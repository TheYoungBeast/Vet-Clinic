package app;

import entity.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PrescriptionCheckFxController implements Initializable
{
    @FXML public TextField prescriptionNo;
    @FXML public Button searchButton;
    @FXML public Label searchInfo;
    @FXML public VBox vboxContent;
    @FXML public TableView<PrescriptedDrugs> drugsTableView;
    @FXML public TableColumn<PrescriptedDrugs, String> drugNameCol;
    @FXML public TableColumn<PrescriptedDrugs, Integer> drugAmountCol;
    @FXML public TextField prescribedBy;

    private final ArrayList<PrescriptedDrugs> prescriptedDrugs = new ArrayList<>();
    private final ArrayList<Drugs> drugs = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vboxContent.setVisible(false);

        drugAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        drugNameCol.setCellFactory(factory -> new TableCell<PrescriptedDrugs, String>() {
            @Override
            protected void updateItem(String s, boolean b) {
                super.updateItem(s, b);

                if(b) {
                    setText(null);
                    return;
                }

                PrescriptedDrugs prescDrug = getTableView().getItems().get(getIndex());
                Drugs drug = drugs.stream().filter(d -> d.getDrugId() == prescDrug.getDrugId()).findFirst().orElse(null);

                if(drug == null){
                    setText(null);
                    return;
                }

                setText(drug.getName());
            }
        });

        EntityManager entityManager = EntityManagerFacade.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            TypedQuery<Drugs> drugsTypedQuery = entityManager.createNamedQuery("Drugs.AllDrugs", Drugs.class);
            drugs.addAll(drugsTypedQuery.getResultList());

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
    }

    @FXML private void OnSearch(ActionEvent event) {
        String text = prescriptionNo.getText();
        if(text == null || text.isEmpty())
            return;

        int prescNo = Integer.valueOf(text);

        if(prescNo < 1)
            return;

        drugsTableView.getItems().clear();
        prescriptedDrugs.clear();

        EntityManager entityManager = EntityManagerFacade.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            TypedQuery<VisitDetails> visitDetailsQuery = entityManager.createNamedQuery("VisitDetails.ByPrescNo", VisitDetails.class);
            visitDetailsQuery.setParameter(1, prescNo);
            VisitDetails visitDetails = visitDetailsQuery.getSingleResult();

            TypedQuery<PrescriptedDrugs> prescriptedDrugsQuery = entityManager.createNamedQuery("PrescriptedDrugs.ByVisitId", PrescriptedDrugs.class);
            prescriptedDrugsQuery.setParameter(1, visitDetails.getVisitId());
            prescriptedDrugs.addAll(prescriptedDrugsQuery.getResultList());

            TypedQuery<Visits> visitQuery = entityManager.createNamedQuery("Visits.ById", Visits.class);
            visitQuery.setParameter(1, visitDetails.getVisitId());
            Visits visit = visitQuery.getSingleResult();

            TypedQuery<Employees> vetQuery = entityManager.createNamedQuery("Employees.GetEmploById", Employees.class);
            vetQuery.setParameter(1, visit.getVetId());
            Employees vet = vetQuery.getSingleResult();

            entityManager.getTransaction().commit();

            prescribedBy.setText(String.format("%s %s", vet.getName(), vet.getSurname()));
            searchInfo.setVisible(false);
            vboxContent.setVisible(true);
        }
        catch (EntityExistsException | NonUniqueResultException | RollbackException exception)
        {
            exception.printStackTrace();
            exception.getCause();

            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
        }
        catch (NoResultException noResultException){
            searchInfo.setVisible(true);
            vboxContent.setVisible(false);
        }
        finally {
            EntityManagerFacade.close();
        }

        drugsTableView.getItems().addAll(prescriptedDrugs);
    }

    @FXML
    private void OnCancel(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }
}
