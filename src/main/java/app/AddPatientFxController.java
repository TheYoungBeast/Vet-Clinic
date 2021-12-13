package app;

import entity.Owners;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.persistence.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddPatientFxController implements Initializable
{
    @FXML
    private Button buttonSearch;
    @FXML
    private Button buttonAdd;
    @FXML
    private TextField inputPetName;
    @FXML
    private ListView<Owners> ownersList;

    @FXML
    private void OnAddOwner(@SuppressWarnings("UnusedParameter")ActionEvent event) {
        AddOwnerFxController controller = new AddOwnerFxController();
        Main.createStage("../AddOwnerPage.fxml", "Vet Clinic: Add Owner", controller);
        controller.setAddPatientController(this);
    }

    public void AddOwnerOnSuccess(Owners owner) {
        ownersList.getItems().add(owner);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            TypedQuery<Owners> allOwnersQuery = entityManager.createNamedQuery("Owners.AllOwners", Owners.class);
            List<Owners> allOwnersList = allOwnersQuery.getResultList();
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
    }
}
