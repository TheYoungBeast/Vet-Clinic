package app;

import entity.Clinics;
import entity.Patients;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddClinicFxController implements Initializable {
    private HomeFxController ctrl = null;


    @FXML private TableView<Clinics> clinicsTableView;

    @FXML private TableColumn<Clinics,String> clinicNameCol;
    @FXML private TableColumn<Clinics,String> clinicZipCodeCol;
    @FXML private TableColumn<Clinics,String> clinicCityCol;
    @FXML private TableColumn<Clinics,String> clinicPhoneNumberCol;
    @FXML private TableColumn<Clinics,String> clinicTypeCol;

    private ArrayList<Clinics> clinics = null;
    private ObservableList<Clinics> clinicList = FXCollections.observableArrayList();
    @FXML
    private void onAddClinic(@SuppressWarnings("UnusedParameter") ActionEvent event){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
    }
    @Override
    public void initialize(URL url, ResourceBundle resources) {

    clinicNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    clinicCityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
    clinicTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    clinicPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    clinicZipCodeCol.setCellValueFactory(new PropertyValueFactory<>("zip"));

        EntityManager entityManager = EntityManagerFacade.createEntityManager();

        try{
            entityManager.getTransaction().begin();

            TypedQuery<Clinics> allClinicsQuery = entityManager.createNamedQuery("Clinics.AllClinics", Clinics.class);
            List<Clinics> allClinicsList = allClinicsQuery.getResultList();

            entityManager.getTransaction().commit();
            clinics = new ArrayList<>(allClinicsList);

            clinicsTableView.setItems(clinicList);

        } catch (EntityExistsException | NonUniqueResultException | RollbackException exception) {
            exception.printStackTrace();
            exception.getCause();

            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
        }
        finally {
            EntityManagerFacade.close();
        }
    }
    public void setHomeController(HomeFxController c) { this.ctrl = c;}
}
