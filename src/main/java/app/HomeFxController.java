package app;

import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class HomeFxController implements Initializable
{
    @FXML
    private Label labelHelloUser;

    @FXML private TextField shiftsFilter;
    @FXML private TextField visitsFilter;

    @FXML private TableView<Shifts> shiftsTableView;
    @FXML private TableView<Visits> visitsTableView;

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
    private ArrayList<Patients> pets = null;
    private ArrayList<Owners> owners = null;

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
        visitHourCol.setCellFactory(factory -> new TableCell<Visits, String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if (b) {
                    setText(null);
                    return;
                }

                Visits visit = getTableView().getItems().get(getIndex());
                if (visit == null) {
                    setText(null);
                    return;
                }

                setText(visit.getDate().toLocalDateTime().toLocalTime().toString().substring(0,5));
            }
        });

        visitVetCol.setCellFactory(factory -> new TableCell<Visits, String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if (b) {
                    setText(null);
                    return;
                }

                Visits visit = getTableView().getItems().get(getIndex());
                if (visit == null) {
                    setText(null);
                    return;
                }

                long vetId = visit.getVetId();
                Employees vet = vets.stream().filter(v -> v.getUsersId() == vetId).findFirst().orElse(null);

                if(vet == null) {
                    setText(null);
                    return;
                }

                setText(String.format("%s %s", vet.getName(), vet.getSurname()));
            }
        });

        visitPetCol.setCellFactory(factory -> new TableCell<Visits, String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if (b) {
                    setText(null);
                    return;
                }

                Visits visit = getTableView().getItems().get(getIndex());
                if (visit == null) {
                    setText(null);
                    return;
                }

                long petId = visit.getPetId();
                Patients pet = pets.stream().filter(p -> p.getPetId() == petId).findFirst().orElse(null);

                if(pet == null) {
                    setText(null);
                    return;
                }

                setText(pet.getPetName());
            }
        });

        visitSpeciesCol.setCellFactory(factory -> new TableCell<Visits, String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if (b) {
                    setText(null);
                    return;
                }

                Visits visit = getTableView().getItems().get(getIndex());
                if (visit == null) {
                    setText(null);
                    return;
                }

                long petId = visit.getPetId();
                Patients pet = pets.stream().filter(p -> p.getPetId() == petId).findFirst().orElse(null);

                if(pet == null) {
                    setText(null);
                    return;
                }

                setText(pet.getSpecies());
            }
        });

        visitBreedCol.setCellFactory(factory -> new TableCell<Visits, String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if (b) {
                    setText(null);
                    return;
                }

                Visits visit = getTableView().getItems().get(getIndex());
                if (visit == null) {
                    setText(null);
                    return;
                }

                long petId = visit.getPetId();
                Patients pet = pets.stream().filter(p -> p.getPetId() == petId).findFirst().orElse(null);

                if(pet == null) {
                    setText(null);
                    return;
                }

                setText(pet.getBreed());
            }
        });

        visitOwnerCol.setCellFactory(factory -> new TableCell<Visits, String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if (b) {
                    setText(null);
                    return;
                }

                Visits visit = getTableView().getItems().get(getIndex());
                if (visit == null) {
                    setText(null);
                    return;
                }

                long petId = visit.getPetId();
                Patients pet = pets.stream().filter(p -> p.getPetId() == petId).findFirst().orElse(null);

                if(pet == null) {
                    setText(null);
                    return;
                }

                long ownerId = pet.getOwnerId();
                Owners owner = HomeFxController.this.owners.stream().filter(o -> o.getOwnerId() == ownerId).findFirst().orElse(null);

                if(owner == null) {
                    setText(null);
                    return;
                }

                setText(String.format("%s %s", owner.getName(), owner.getSurname()));
            }
        });

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
            pets = new ArrayList<>( allPatientsQuery.getResultList() );

            TypedQuery<Owners> allOwnersQuery = entityManager.createNamedQuery("Owners.AllOwners", Owners.class);
            owners = new ArrayList<>( allOwnersQuery.getResultList() );

            Query allVisitsToday = entityManager.createNativeQuery("SELECT * from VISITS v where trunc(v.VISIT_DATE) = TO_DATE(TO_CHAR(cast(sysdate as date),'YYYY-MM-DD'))", Visits.class);
            visits.addAll(allVisitsToday.getResultList());

            entityManager.getTransaction().commit();

            FilteredList<Visits> visitsFilteredList = new FilteredList<>(visits, p -> true);
            FilteredList<Shifts> shiftsFilteredList = new FilteredList<>(shifts, p -> true);

            visitsFilter.textProperty().addListener(((observableValue, s, t1) -> {
                visitsFilteredList.setPredicate(visit -> {
                    if(t1 == null || t1.isEmpty())
                        return true;

                    String keyword = t1.toLowerCase();

                    Patients pet = pets.stream().filter(p -> p.getPetId() == visit.getPetId()).findFirst().orElse(null);
                    if(pet == null)
                        return false;

                    Owners owner = owners.stream().filter(o -> o.getOwnerId() == pet.getOwnerId()).findFirst().orElse(null);
                    if (owner == null)
                        return false;

                    Employees vet = vets.stream().filter(v -> v.getUsersId() == visit.getVetId()).findFirst().orElse(null);

                    if (pet.getPetName().toLowerCase().contains(keyword) || pet.getSpecies().toLowerCase().contains(keyword) ||
                            pet.getBreed().toLowerCase().contains(keyword) || owner.getName().toLowerCase().contains(keyword) ||
                            owner.getSurname().toLowerCase().contains(keyword) || vet.getSurname().toLowerCase().contains(keyword) || vet.getName().toLowerCase().contains(keyword))
                        return true;

                    return false;
                });
            }));

            shiftsFilter.textProperty().addListener(((observableValue, s, t1) -> {
                shiftsFilteredList.setPredicate(shift -> {
                    if(t1 == null || t1.isEmpty())
                        return true;

                    String keyword = t1.toLowerCase();

                    Employees vet = vets.stream().filter(v -> v.getUsersId() == shift.getVetId()).findFirst().orElse(null);

                    if(vet.getName().toLowerCase().contains(keyword) || vet.getSurname().toLowerCase().contains(keyword) ||
                            shift.getStartDate().toLocalDateTime().toString().toLowerCase().contains(keyword) || shift.getEndDate().toLocalDateTime().toString().toLowerCase().contains(keyword))
                        return true;

                    return false;
                });
            }));

            SortedList<Visits> visitsSortedList = new SortedList<>(visitsFilteredList);
            visitsSortedList.comparatorProperty().bind(visitsTableView.comparatorProperty());

            SortedList<Shifts> shiftsSortedList = new SortedList<>(shiftsFilteredList);
            shiftsSortedList.comparatorProperty().bind(shiftsTableView.comparatorProperty());

            shiftsTableView.setItems(shiftsSortedList);
            visitsTableView.setItems(visitsSortedList);

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
        pets.add(p);
    }

    public void OnAddOwnerSuccessful(Owners o) {
        owners.add(o);
    }

    @FXML void OnAddVisit(@SuppressWarnings("UnusedParameter")ActionEvent event) {
        Main.createStage("../AddVisitPage.fxml", "Vet Clinic: Add Visit");
    }
}
