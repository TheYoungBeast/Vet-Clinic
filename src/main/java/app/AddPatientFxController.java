package app;

import entity.Owners;
import entity.Patients;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

interface Command
{
    void setDelegate(Object o);
    void execute(Object o);
}

public class AddPatientFxController implements Initializable
{
    private HomeFxController homeController;

    @FXML
    private Button buttonAdd;
    @FXML
    private TextField inputPetName, inputOwner, inputSex, inputSpecies, inputBreed;
    @FXML
    private TextField inputSearch;
    @FXML
    private DatePicker inputBirthDate;
    @FXML
    private ListView<Owners> ownersList;

    private Owners selectedOwner = null;
    private List<Owners> allOwners;

    private Command command = new Command() {
        private AddPatientFxController ctrl;

        @Override
        public void setDelegate(Object o) {
            ctrl = (AddPatientFxController) o;
        }

        @Override
        public void execute(Object o) {
            AddOwnerFxController con = (AddOwnerFxController) o;
            con.setAddPatientController(ctrl);
        }
    };

    @FXML
    private void OnAddOwner(@SuppressWarnings("UnusedParameter")ActionEvent event) {
        command.setDelegate(this);
        Main.createStage("../AddOwnerPage.fxml", "Vet Clinic: Add Owner", command);
    }

    public void AddOwnerOnSuccess(Owners owner) {
        allOwners.add(owner);
        ownersList.getItems().clear();
        ownersList.getItems().addAll(allOwners);
        inputSearch.setText(null);

        homeController.OnAddOwnerSuccessful(owner);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            TypedQuery<Owners> allOwnersQuery = entityManager.createNamedQuery("Owners.AllOwners", Owners.class);
            List<Owners> allOwnersList = allOwnersQuery.getResultList();
            allOwners = allOwnersList;
            ownersList.getItems().addAll(allOwnersList);
            entityManager.getTransaction().commit();
        } catch (EntityExistsException | NonUniqueResultException | RollbackException exception) {
            exception.printStackTrace();
            exception.getCause();

            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        ownersList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Owners>() {
            @Override
            public void changed(ObservableValue<? extends Owners> owner, Owners owners, Owners t1) {
                Owners selectedItem = ownersList.getSelectionModel().getSelectedItem();
                if(selectedItem != null) {
                    selectedOwner = selectedItem;
                    inputOwner.setText(String.valueOf(selectedOwner));
                }
            }
        });
    }

    @FXML
    private void OnPatientAdd(ActionEvent event)
    {
        buttonAdd.setDisable(true);

        String petName = inputPetName.getText().trim();
        String petSex = inputSex.getText().trim().toUpperCase();
        String petSpecies = inputSpecies.getText().trim();
        String petBreed = inputBreed.getText().trim();
        LocalDate date = inputBirthDate.getValue();
        Date petBirthDate = new Date(date.getYear()-1900, date.getMonthValue()-1, date.getDayOfMonth());
        //petBirthDate.setTime(0);

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Patients patient = new Patients();

        try {
            entityManager.getTransaction().begin();

            Query nativeQuery = entityManager.createNativeQuery("SELECT SBD_ST_PS6_4.PATIENTS_IDS_SEQUENCE.nextval as id FROM DUAL");
            long id = ((BigDecimal) nativeQuery.getSingleResult()).longValue();

            patient.setOwnerId(selectedOwner.getOwnerId());
            patient.setBirthDate(petBirthDate);
            patient.setBreed(petBreed);
            patient.setSex(petSex);
            patient.setSpecies(petSpecies);
            patient.setPetName(petName);
            patient.setPetId(id);

            entityManager.persist(patient);

            entityManager.getTransaction().commit();
        }
        catch (EntityExistsException | NonUniqueResultException | RollbackException exception)
        {
            exception.printStackTrace();
            exception.getCause();

            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
        }
        finally
        {
            entityManager.close();
            entityManagerFactory.close();
            buttonAdd.setDisable(false);
        }

        homeController.OnAddPatientSuccessful(patient);
        ((Stage)buttonAdd.getScene().getWindow()).close();
    }

    @FXML
    private void OnCancel(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    private void OnSearchList(ActionEvent event)
    {
        if(inputSearch.getText() != null && !inputSearch.getText().trim().isEmpty()) {
            ownersList.getItems().clear();
            ownersList.getItems().addAll(searchList(inputSearch.getText(), allOwners));
        }
        else {
            if(ownersList.getItems().size() != allOwners.size()) {
                ownersList.getItems().clear();
                ownersList.getItems().addAll(allOwners);
            }
        }
    }

    private List<Owners> searchList(String keyword, List<Owners> list)
    {
        List<String> searchWords = Arrays.asList(keyword.trim().split(" "));

        return list.stream().filter(input -> {
            return searchWords.stream().allMatch(word -> {
                return input.getName().toLowerCase().contains(word.toLowerCase()) ||
                        input.getSurname().toLowerCase().contains(word.toLowerCase());
            });
        }).collect(Collectors.toList());
    }

    public void setHomeController(HomeFxController ctrl) {
        this.homeController = ctrl;
    }
}
