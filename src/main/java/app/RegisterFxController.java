package app;

import entity.Employees;
import entity.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.persistence.*;
import java.math.BigDecimal;

public class RegisterFxController
{
    @FXML
    private TextField inputUsername;
    @FXML
    private PasswordField inputPassword;
    @FXML
    private PasswordField inputConfirmPassword;
    @FXML
    private TextField inputEmail;
    @FXML
    private Label labelRegisterInfo;
    @FXML
    private TextField inputName;
    @FXML
    private TextField inputSurname;
    @FXML
    private TextField inputPesel;
    @FXML
    private TextArea inputAdress;

    @FXML
    private void onSignIn(@SuppressWarnings("UnusedParameters") ActionEvent event)
    {
        labelRegisterInfo.setVisible(false);

        if(inputPassword.getText().equals(inputConfirmPassword.getText())) {

            String username = inputUsername.getText().replaceAll("\\s+","");
            String password = inputPassword.getText().replaceAll("\\s+","");
            String email = inputEmail.getText().replaceAll("\\s+","");
            String name = inputName.getText().replaceAll("\\s+","");
            String pesel = inputPesel.getText().replaceAll("\\s+","");

            if(username.isEmpty() || password.isEmpty() || email.isEmpty() || name.isEmpty() || pesel.isEmpty() ||
                inputSurname.getText().isEmpty() || inputAdress.getText().isEmpty() )
            {
                labelRegisterInfo.setVisible(true);
                labelRegisterInfo.setText("Fields cannot be empty");
            }
            else
            {
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                Query nativeQuery = entityManager.createNativeQuery("SELECT SBD_ST_PS6_4.USERID_SEQUENCE.nextval as id FROM DUAL");

                long id = ((BigDecimal) nativeQuery.getSingleResult()).longValue();
                Users user = new Users();
                user.setId(id);
                user.setLogin(inputUsername.getText());
                user.setPassword(inputPassword.getText());
                user.setEmail(inputEmail.getText());

                System.out.print(user);

                try
                {
                    entityManager.getTransaction().begin();
                    entityManager.persist(user);
                    entityManager.getTransaction().commit();
                }
                catch (EntityExistsException | NonUniqueResultException | RollbackException exception)
                {
                    labelRegisterInfo.setVisible(true);
                    labelRegisterInfo.setText(exception.getMessage());
                    if(entityManager.getTransaction().isActive())
                        entityManager.getTransaction().rollback();
                    return;
                }

                try
                {
                    System.out.print(String.format("UserId: %d", user.getId()));
                    Employees employee = new Employees();
                    employee.setUsersId(user.getId());
                    employee.setName(name);
                    employee.setSurname(inputSurname.getText());
                    employee.setPesel(Long.valueOf(pesel));
                    employee.setAdress(inputAdress.getText());
                    employee.setPosition("Employee");

                    entityManager.getTransaction().begin();
                    entityManager.persist(employee);
                    entityManager.getTransaction().commit();
                }
                catch (EntityExistsException | NonUniqueResultException | RollbackException exception)
                {
                    labelRegisterInfo.setVisible(true);
                    labelRegisterInfo.setText(exception.getMessage());
                    if(entityManager.getTransaction().isActive())
                        entityManager.getTransaction().rollback();
                    return;
                }
                finally {
                    entityManager.close();
                    entityManagerFactory.close();
                }

                labelRegisterInfo.setText("Rejestracja przebiegła pomyślnie");
                labelRegisterInfo.setTextFill(Color.GREEN);
                Main.createStage("../LoginPage.fxml", "Vet Clinic: Login");
                ((Stage) inputUsername.getScene().getWindow()).close();
            }
        }
        else {
            labelRegisterInfo.setVisible(true);
            labelRegisterInfo.setText("Passwords do not match");
        }
    }

    @FXML
    private void onCancel(@SuppressWarnings("UnusedParameters") ActionEvent event) {
        ((Stage) inputUsername.getScene().getWindow()).close();
        Main.createStage("../LoginPage.fxml", "Vet Clinic: Login");
    }

}
