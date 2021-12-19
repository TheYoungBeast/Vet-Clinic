package app;

import entity.Employees;
import entity.Patients;
import entity.Shifts;
import entity.Visits;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeFxController implements Initializable
{
    @FXML
    private Label labelHelloUser;

    @FXML private TextField shiftsFilter;
    @FXML private TextField visitsFilter;

    @FXML private TableView<Shifts> shiftsTableView;

    @FXML private TableColumn<Shifts, String> shiftStartDateCol;
    @FXML private TableColumn<Shifts, String> shiftEndDateCol;
    @FXML private TableColumn<Shifts, String> shiftStartHourCol;
    @FXML private TableColumn<Shifts, String> shiftEndHourCol;
    @FXML private TableColumn<Shifts, String> shiftNameCol;
    @FXML private TableColumn<Shifts, String> shiftSurnameCol;

    @FXML private TableColumn<Visits, String> visitHourCol;
    @FXML private TableColumn<Visits, String> visitVetCol;
    @FXML private TableColumn<Visits, String> visitPetCol;
    @FXML private TableColumn<Visits, String> visitSpeciesCol;
    @FXML private TableColumn<Visits, String> visitBreedCol;
    @FXML private TableColumn<Visits, String> visitOwnerCol;

    private ObservableList<Shifts> shifts = FXCollections.observableArrayList();
    private ObservableList<Visits> visits = FXCollections.observableArrayList();

    private ArrayList<Employees> vets = null;

    private final Command command = new Command() {
        private HomeFxController ctrl;
        @Override
        public void setDelegate(Object o) {
            ctrl = (HomeFxController) o;
        }

        @Override
        public void execute(Object o) {
            AddPatientFxController con = (AddPatientFxController) o;
            con.setHomeController(ctrl);
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // shifts cell factory

        shiftStartDateCol.setCellFactory(factory -> new TableCell<Shifts, String>() {
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);

                if (empty)
                    setText(null);
                else {
                    Shifts shift = getTableView().getItems().get(getIndex());
                    if(shift != null )
                        setText(String.valueOf(shift.getStartDate().toLocalDateTime().toLocalDate()));
                    else
                        setText(null);
                }
            }
        });

        shiftEndDateCol.setCellFactory(factory -> new TableCell<Shifts, String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if(b)
                    setText(null);
                else {
                    Shifts shift = getTableView().getItems().get(getIndex());
                    if(shift != null )
                        setText(String.valueOf(shift.getEndDate().toLocalDateTime().toLocalDate()));
                    else
                        setText(null);
                }
            }
        });

        shiftStartHourCol.setCellFactory(factory -> new TableCell<Shifts, String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if(b)
                    setText(null);
                else {
                    Shifts shift = getTableView().getItems().get(getIndex());
                    if(shift != null )
                        setText(String.valueOf(shift.getStartDate().toLocalDateTime().toLocalTime()).substring(0, 5));
                    else
                        setText(null);
                }
            }
        });

        shiftEndHourCol.setCellFactory(factory -> new TableCell<Shifts, String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if(b)
                    setText(null);
                else {
                    Shifts shift = getTableView().getItems().get(getIndex());
                    if(shift != null )
                        setText(String.valueOf(shift.getEndDate().toLocalDateTime().toLocalTime()).substring(0, 5));
                    else
                        setText(null);
                }
            }
        });

        shiftNameCol.setCellFactory(factory -> new TableCell<Shifts, String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if(b){
                    setText(null);
                    return;
                }

                Shifts shift = getTableView().getItems().get(getIndex());
                if(shift == null) {
                    setText(null);
                    return;
                }

                Employees vet = vets.stream().filter(e -> e.getUsersId() == shift.getVetId()).findFirst().orElse(null);
                if(vet == null) {
                    setText(null);
                    return;
                }

                setText(vet.getName());
            }
        });

        shiftSurnameCol.setCellFactory(factory -> new TableCell<Shifts, String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if(b){
                    setText(null);
                    return;
                }

                Shifts shift = getTableView().getItems().get(getIndex());
                if(shift == null) {
                    setText(null);
                    return;
                }

                Employees vet = vets.stream().filter(e -> e.getUsersId() == shift.getVetId()).findFirst().orElse(null);
                if(vet == null) {
                    setText(null);
                    return;
                }

                setText(vet.getSurname());
            }
        });

        // visits cell factory
        visitHourCol.setCellFactory(factory -> new TableCell<Visits, String>());

        EntityManager entityManager = EntityManagerFacade.createEntityManager();

        try
        {
            entityManager.getTransaction().begin();

            TypedQuery<Employees> namedQuery = entityManager.createNamedQuery("Employees.GetEmploById", Employees.class);
            namedQuery.setParameter(1, LoggedUserInfo.getInstance().getUser().getId());
            Employees employee = namedQuery.getSingleResult();

            TypedQuery<Employees> allVetsQuery = entityManager.createNamedQuery("Employees.GetVets", Employees.class);
            vets = new ArrayList<>(allVetsQuery.getResultList());

            TypedQuery<Shifts> allShiftQuery = entityManager.createNamedQuery("SHIFTS.GetAll", Shifts.class);
            shifts.addAll(allShiftQuery.getResultList());

            TypedQuery<Patients> allPatientsQuery = entityManager.createNamedQuery("Patients.AllPatients", Patients.class);
            List<Patients> resultPatients = allPatientsQuery.getResultList();
            entityManager.getTransaction().commit();

            shiftsTableView.setItems(shifts);

            LoggedUserInfo.getInstance().setEmployee(employee);
        }
        catch (EntityExistsException | NonUniqueResultException | RollbackException exception)
        {
            exception.printStackTrace();
            exception.getCause();

            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
        }
        finally {
            EntityManagerFacade.close();
        }

        String helloMsg = String.format(labelHelloUser.getText(), LoggedUserInfo.getInstance().getEmployee().getName(),
                LoggedUserInfo.getInstance().getEmployee().getSurname());
        labelHelloUser.setText(helloMsg);
    }
    @FXML
    private void OnAddPatient(@SuppressWarnings("UnusedParameter")ActionEvent event) {
        command.setDelegate(this);
        Main.createStage("../AddPatientPage.fxml", "Vet Clinic: Add Patient", command);
    }

    public void OnAddPatientSuccessful(Patients p) {
        //patientList.getItems().add(p);
    }

    @FXML void OnAddVisit(@SuppressWarnings("UnusedParameter")ActionEvent event) {
        Main.createStage("../AddVisitPage.fxml", "Vet Clinic: Add Visit");
    }
}
