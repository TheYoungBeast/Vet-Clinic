package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application
{
    public static final String appIconPath = "../blue.jpg";
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Main.createStage("../LoginPage.fxml", "Vet Clinic: Login");
    }

    public static Stage createStage(String fxmlFilePath, String title)
    {
        Stage stage = new Stage();
        try
        {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlFilePath));
            Scene scene = new Scene(root);
            stage.getIcons().add(new Image(Main.class.getResourceAsStream(Main.appIconPath)));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(title);
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            e.getCause();
        }

        return stage;
    }
}
