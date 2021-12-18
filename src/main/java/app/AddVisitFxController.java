package app;

import entity.Employees;
import entity.Owners;
import entity.Patients;
import entity.Veterinarians;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.persistence.*;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddVisitFxController implements Initializable
{
    @FXML private TextField selectedPatientField;
    @FXML private TextField selectedVetField;

    @FXML private TextField petTableFilter;
    @FXML private TextField vetTableFilter;

    @FXML private TableView<Patients> patientsTableView;
    @FXML private TableView<Employees> vetsTableView;

    @FXML private TableColumn<Patients, String> petNameColumn;
    @FXML private TableColumn<Patients, String> sexColumn;
    @FXML private TableColumn<Patients, String> speciesColumn;
    @FXML private TableColumn<Patients, String> breedColumn;
    @FXML private TableColumn<Patients, Date> birthDateColumn;
    @FXML private TableColumn<Patients, String> ownerColumn;

    @FXML private TableColumn<Employees, String> vetNameColumn;
    @FXML private TableColumn<Employees, String> vetSurnameColumn;
    @FXML private TableColumn<Employees, Long> vetPwzColumn;

    private ObservableList<Patients> patientsList = FXCollections.observableArrayList();
    private ObservableList<Employees> employeesList = FXCollections.observableArrayList();

    private ArrayList<Owners> owners = null;
    private ArrayList<Employees> employees = null;
    private ArrayList<Veterinarians> vets = null;

    private Employees selectedVet = null;
    private Patients selectedPatient = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        petNameColumn.setCellValueFactory(new PropertyValueFactory<>("petName"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        breedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        ownerColumn.setCellFactory(factory -> new TableCell<Patients, String>(){
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if(empty)
                    setText(null);
                else
                {
                    Patients patient = getTableView().getItems().get(getIndex());
                    if(patient != null) {
                        Owners owner = owners.stream().filter(o -> o.getOwnerId() == patient.getOwnerId()).findFirst().orElse(null);

                        if(owner != null)
                            setText(String.format("%s %s", owner.getName(), owner.getSurname()));
                        else setText(null);
                    }
                    else
                        setText(null);
                }
            }
        });

        vetNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        vetSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        vetPwzColumn.setCellFactory(factory -> new TableCell<Employees, Long>(){
            @Override
            protected void updateItem(Long pwz, boolean empty) {
                super.updateItem(pwz, empty);

                if(empty)
                    setText(null);
                else {
                    Employees employees = getTableView().getItems().get(getIndex());

                    if(employees != null) {
                        Veterinarians vet = vets.stream().filter(v -> v.getVetId() == employees.getUsersId()).findFirst().orElse(null);

                        if (vet != null)
                            setText(String.valueOf(vet.getPwzNo()));
                        else setText(null);
                    }
                    else setText(null);
                }
            }
        });

        EntityManager entityManager = EntityManagerFacade.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            TypedQuery<Patients> allPatientsQuery = entityManager.createNamedQuery("Patients.AllPatients", Patients.class);
            List<Patients> resultPatients = allPatientsQuery.getResultList();

            TypedQuery<Owners> allOwnersQuery = entityManager.createNamedQuery("Owners.AllOwners", Owners.class);
            List<Owners> allOwnersList = allOwnersQuery.getResultList();

            TypedQuery<Employees> allVetsQuery = entityManager.createNamedQuery("Employees.GetVets", Employees.class);
            List<Employees> allVetsList = allVetsQuery.getResultList();

            TypedQuery<Veterinarians> allVetsDetailsQuery = entityManager.createNamedQuery("Veterinarians.GetAll", Veterinarians.class);
            List<Veterinarians> allVetsDetailsList = allVetsDetailsQuery.getResultList();

            entityManager.getTransaction().commit();

            employees = new ArrayList<>(allVetsList);
            owners = new ArrayList<>(allOwnersList);
            vets = new ArrayList<>(allVetsDetailsList);

            patientsList.addAll(resultPatients);
            employeesList.addAll(allVetsList);
        }
        catch (EntityExistsException | NonUniqueResultException | RollbackException exception) {
            exception.printStackTrace();
            exception.getCause();

            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
        }
        finally {
            EntityManagerFacade.close();
        }


        FilteredList<Patients> filteredPatientsList = new FilteredList<>(patientsList, b -> true);
        FilteredList<Employees> filteredEmployeesList = new FilteredList<>(employeesList, b -> true);

        petTableFilter.textProperty().addListener((observableValue, s, t1) -> {
            filteredPatientsList.setPredicate(patient -> {
                if(t1 == null || t1.isEmpty())
                    return true;

                String keyword = t1.toLowerCase();

                if(patient.getBreed().toLowerCase().contains(keyword) || patient.getSpecies().toLowerCase().contains(keyword)
                    || patient.getPetName().toLowerCase().contains(keyword) || patient.getSex().toLowerCase().contains(keyword))
                    return true;

                return false;
            });
        });

        vetTableFilter.textProperty().addListener((observableValue, s, t1) -> {
            filteredEmployeesList.setPredicate(employee -> {
                if(t1 == null || t1.isEmpty())
                    return true;

                String keyword = t1.toLowerCase();
                Veterinarians vet = vets.stream().filter(v -> v.getVetId() == employee.getUsersId()).findFirst().orElse(null);

                if(employee.getName().toLowerCase().contains(keyword) || employee.getSurname().toLowerCase().contains(keyword)
                    || (vet != null && String.valueOf(vet.getPwzNo()).contains(keyword)) )
                    return true;

                return false;
            });
        });

        SortedList<Patients> sortedPatientsList = new SortedList<>(filteredPatientsList);
        sortedPatientsList.comparatorProperty().bind(patientsTableView.comparatorProperty());

        SortedList<Employees> sortedEmploList = new SortedList<>(filteredEmployeesList);
        sortedEmploList.comparatorProperty().bind(vetsTableView.comparatorProperty());

        patientsTableView.setItems(sortedPatientsList);
        vetsTableView.setItems(sortedEmploList);

        patientsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Patients>() {
            @Override
            public void changed(ObservableValue<? extends Patients> observableValue, Patients patients, Patients t1) {
                if(t1 != null) {
                    selectedPatientField.setText(String.format("%s %s %s", t1.getPetName(), t1.getSpecies(), t1.getBreed()));
                    selectedPatient = t1;
                }
                else {
                    selectedPatientField.setText(null);
                    selectedPatient = null;
                }
            }
        });

        vetsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Employees>() {
            @Override
            public void changed(ObservableValue<? extends Employees> observableValue, Employees employees, Employees t1) {
                if(t1 != null) {
                    selectedVetField.setText(String.format("%s %s", t1.getName(), t1.getSurname()));
                    selectedVet = t1;
                }
                else {
                    selectedVetField.setText(null);
                    selectedVet = null;
                }
            }
        });
    }
}
