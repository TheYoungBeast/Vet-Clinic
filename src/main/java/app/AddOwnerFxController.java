package app;

import entity.Owners;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.*;
import java.math.BigDecimal;

public class AddOwnerFxController
{
    private AddPatientFxController controller;

    @FXML
    private TextField inputName;
    @FXML
    private TextField inputSurname;
    @FXML
    private TextField inputPhone;
    @FXML
    private TextField inputEmail;

    @FXML
    private void OnAddOwner(@SuppressWarnings("UnusedParameter")ActionEvent event)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Owners owner = new Owners();
        owner.setName(inputName.getText());
        owner.setSurname(inputSurname.getText());
        owner.setPhoneeNumber(Long.valueOf(inputPhone.getText().replaceAll("-", "").replaceAll("\\s+", "")));
        owner.setEmail(inputEmail.getText());

        try
        {
            entityManager.getTransaction().begin();

            Query nativeQuery = entityManager.createNativeQuery("SELECT SBD_ST_PS6_4.OWNERS_IDS_SEQUENCE.nextval as id FROM DUAL");
            long id = ((BigDecimal) nativeQuery.getSingleResult()).longValue();
            owner.setOwnerId(id);

            entityManager.persist(owner);
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
        }

        controller.AddOwnerOnSuccess(owner);

        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }
    
    @FXML
    private void OnCancel(ActionEvent event)
    {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    public void setAddPatientController(AddPatientFxController c) {
        this.controller = c;
    }
}
