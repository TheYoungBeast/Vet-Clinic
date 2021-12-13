package app;

import entity.Employees;
import entity.Patients;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.persistence.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeFxController implements Initializable
{
    @FXML
    private Label labelHelloUser;
    @FXML
    private ListView<Patients> patientList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try
        {
            entityManager.getTransaction().begin();
            TypedQuery<Employees> namedQuery = entityManager.createNamedQuery("Employees.GetEmploById", Employees.class);
            namedQuery.setParameter(1, LoggedUserInfo.getInstance().getUser().getId());
            Employees employee = namedQuery.getSingleResult();

            TypedQuery<Patients> allPatientsQuery = entityManager.createNamedQuery("Patients.AllPatients", Patients.class);
            List<Patients> resultPatients = allPatientsQuery.getResultList();
            entityManager.getTransaction().commit();

            patientList.getItems().addAll(resultPatients);

            LoggedUserInfo.getInstance().setEmployee(employee);
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

        String helloMsg = String.format(labelHelloUser.getText(), LoggedUserInfo.getInstance().getEmployee().getName(),
                LoggedUserInfo.getInstance().getEmployee().getSurname());
        labelHelloUser.setText(helloMsg);
    }
    @FXML
    private void OnAddPatient(@SuppressWarnings("UnusedParameter")ActionEvent event) {
        Main.createStage("../AddPatientPage.fxml", "Vet Clinic: Add Patient");
    }
}
