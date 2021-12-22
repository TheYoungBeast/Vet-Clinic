package app;

import entity.VisitDetails;
import entity.Visits;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.RollbackException;
import java.net.URL;
import java.util.ResourceBundle;

public class VisitsDetailsFxController implements Initializable
{
    private static final int maxLength = 2048;

    private Visits visit;
    private long prescriptionNumber = 0;

    @FXML private AnchorPane anchorPane;
    @FXML private Label statusInfo;
    @FXML private CheckBox statusCheckbox;
    @FXML private TextField priceTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private TextField prescriptionNo;
    @FXML private Button submitButton;
    @FXML private Button prescribeButton;

    private CommandVisitDetail commandVisitDetail = new CommandVisitDetail() {
        Visits visit = null;
        @Override
        public void setVisit(Visits v) {
            visit = v;
        }

        @Override
        public void setDelegate(Object o) {
            // empty
        }

        @Override
        public void execute(Object o) {
            PrescribePrescriptionFxController controller = (PrescribePrescriptionFxController) o;
            controller.setVisit(visit);
            controller.setVisitDetailController(VisitsDetailsFxController.this);
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        anchorPane.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        if(visit == null || visit.getStatus().toLowerCase().trim().equals("done")) {
                            submitButton.setDisable(true);
                            statusCheckbox.setDisable(true);
                            statusInfo.setVisible(true);
                            prescribeButton.setDisable(true);
                        }
                    }
                });
            }
        });

        statusCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue == null)
                    return;

                priceTextField.setDisable(!newValue);
                descriptionTextArea.setDisable(!newValue);
                prescriptionNo.setDisable(!newValue);
                prescribeButton.setDisable(!newValue);
            }
        });

        descriptionTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (descriptionTextArea.getText().length() > maxLength) {
                    String s = descriptionTextArea.getText().substring(0, maxLength);
                    descriptionTextArea.setText(s);
                }
            }
        });

        priceTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    priceTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public void setVisit(Visits visit) {
        this.visit = visit;
    }

    @FXML private void OnSubmit(ActionEvent event) {

        if(visit == null || visit.getStatus().toLowerCase().equals("done") || priceTextField.getText() == null || priceTextField.getText().trim().isEmpty())
            return;

        EntityManager entityManager = EntityManagerFacade.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            VisitDetails visitDetails = new VisitDetails();
            visitDetails.setVisitId(visit.getVisitId());
            visitDetails.setCena(Integer.valueOf(priceTextField.getText()));
            visitDetails.setDescription(descriptionTextArea.getText());

            if(prescriptionNo.getText() != null && !prescriptionNo.getText().trim().isEmpty())
                visitDetails.setPrescriptionNo(Integer.valueOf(prescriptionNo.getText()));

            entityManager.persist(visitDetails);

            // update visit
            visit.setStatus("DONE");
            Session session = entityManager.unwrap(Session.class);
            session.update(visit);

            entityManager.getTransaction().commit();
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

        this.OnCancel(event);
    }

    @FXML private void OnCancel(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    public void setPrescriptionNumber(long number) {
        prescriptionNumber = number;
        prescriptionNo.setText(String.valueOf(prescriptionNumber));
    }

    public void OnPrescribe(ActionEvent event) {
        commandVisitDetail.setVisit(visit);
        Main.createStage("../PrescribePrescriptionPage.fxml", "Vet Clinic: Make Prescription", commandVisitDetail);
    }
}
