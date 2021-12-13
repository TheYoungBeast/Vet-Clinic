package app;

import entity.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class LoginFxController
{
    @FXML
    private Button buttonLogin;
    @FXML
    private Label labelLoginInfo;
    @FXML
    private TextField inputUser;
    @FXML
    private PasswordField inputPassword;

    @FXML
    private void LoginOnAction(ActionEvent event)
    {
        if(buttonLogin.isDisable())
            return;

        buttonLogin.setDisable(true);

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        TypedQuery<Users> namedQuery = entityManager.createNamedQuery("Users.Login", Users.class);
        namedQuery.setParameter(1, inputUser.getText());
        namedQuery.setParameter(2, inputPassword.getText());

        if(namedQuery.getResultList().isEmpty())
            labelLoginInfo.setVisible(true);
        else
        {
            LoggedUserInfo.getInstance().setUser(namedQuery.getSingleResult());
            labelLoginInfo.setVisible(true);
            labelLoginInfo.setText("Logowanie poprawne");
            labelLoginInfo.setTextFill(Color.GREEN);
            Main.createStage("../HomePage.fxml", "Vet Clinic");
            ((Stage) buttonLogin.getScene().getWindow()).close();
        }

        entityManager.getTransaction().commit();

        buttonLogin.setDisable(false);
    }

    @FXML
    private void OnRegisterNow(@SuppressWarnings("UnusedParameters") ActionEvent event)
    {
        ((Stage) buttonLogin.getScene().getWindow()).close();
        Main.createStage("../RegisterPage.fxml", "Vet Clinic: Registration");
    }
}
