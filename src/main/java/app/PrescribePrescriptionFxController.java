package app;

import entity.Drugs;
import entity.PrescriptedDrugs;
import entity.PrescriptedDrugsPK;
import entity.Visits;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PrescribePrescriptionFxController implements Initializable
{
    private VisitsDetailsFxController controller;
    private long presciptionNumber = 0;
    private Visits visit = null;

    @FXML private AnchorPane anchorPane;
    @FXML private TextField prescriptionNo;

    @FXML private TableView<Drugs> drugsTableView;
    @FXML private TableView<Drugs> prescriptedDrugsTableView;

    @FXML private TableColumn<Drugs, String> prescriptedDrugCol;
    @FXML private TableColumn<Drugs, Integer> prescriptedAmountCol;

    @FXML private TableColumn<Drugs, String> drugNameCol;
    @FXML private TableColumn<Drugs, String> dosageCol;
    @FXML private TableColumn<Drugs, Double> priceCol;

    @FXML private Button prescribeDrugButton;

    @FXML private TextField searchFilter;

    private Drugs selectedDrug = null;
    private ObservableList<Drugs> drugsObservableList = FXCollections.observableArrayList();
    private HashMap<Drugs, Integer> prescibedDrugs = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        prescriptedDrugCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prescriptedAmountCol.setCellValueFactory(c -> {
            ObservableValue<Integer> integerObservableValue = new SimpleIntegerProperty(1).asObject();
            return integerObservableValue;
        });

        prescriptedDrugsTableView.setEditable(true);
        prescriptedAmountCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        prescriptedAmountCol.setEditable(true);

        drugNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dosageCol.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        EntityManager entityManager = EntityManagerFacade.createEntityManager();
        long id = 0;

        try {
            entityManager.getTransaction().begin();

            Query nativeQuery = entityManager.createNativeQuery("SELECT SBD_ST_PS6_4.PRESCRIPTION_ID_SEQUENCE.nextval as id FROM DUAL");
            presciptionNumber = id = ((BigDecimal) nativeQuery.getSingleResult()).longValue();

            TypedQuery<Drugs> allDrugsQuery = entityManager.createNamedQuery("Drugs.AllDrugs", Drugs.class);
            drugsObservableList.addAll(allDrugsQuery.getResultList());

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

        FilteredList<Drugs> drugsFilteredList = new FilteredList<>(drugsObservableList, p -> true);

        searchFilter.textProperty().addListener(((observableValue, s, t1) -> {
            drugsFilteredList.setPredicate(drug -> {
                if(t1 == null || t1.isEmpty())
                    return true;

                String keyword = t1.toLowerCase();

                if(drug.getName().toLowerCase().contains(keyword))
                    return true;

                return false;
            });
        }));

        SortedList<Drugs> drugsSortedList = new SortedList<>(drugsFilteredList);
        drugsSortedList.comparatorProperty().bind(drugsTableView.comparatorProperty());

        drugsTableView.setItems(drugsSortedList);
        prescriptionNo.setText(String.valueOf(id));

        drugsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Drugs>() {
            @Override
            public void changed(ObservableValue<? extends Drugs> observableValue, Drugs drugs, Drugs t1) {
                if(t1 != null) {
                    prescribeDrugButton.setDisable(false);
                    selectedDrug = t1;
                }
                else
                    prescribeDrugButton.setDisable(true);
            }
        });
    }

    public void OnPrescribeDrug(ActionEvent event) {
        if(selectedDrug == null) {
            prescribeDrugButton.setDisable(true);
            return;
        }

        if(prescriptedDrugsTableView.getItems().contains(selectedDrug))
            return;

        prescriptedDrugsTableView.getItems().add(selectedDrug);
        prescibedDrugs.put(selectedDrug, 1);
    }

    public void OnEditCommit(TableColumn.CellEditEvent<Drugs, Integer> drugsIntegerCellEditEvent) {
        Integer value = drugsIntegerCellEditEvent.getNewValue();
        Drugs drug = drugsIntegerCellEditEvent.getRowValue();
        prescibedDrugs.replace(drug, value);
    }

    @FXML private void OnCancel(ActionEvent event)
    {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    @FXML public void OnPrescribe(ActionEvent event) {

        if(visit == null)
            return;

        EntityManager entityManager = EntityManagerFacade.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            prescibedDrugs.forEach((key, value) -> {
                PrescriptedDrugs prescriptedDrug = new PrescriptedDrugs();
                prescriptedDrug.setDrugId(key.getDrugId());
                prescriptedDrug.setVisitId(visit.getVisitId());
                prescriptedDrug.setAmount(value.intValue());

                System.out.print("Drug id: " + String.valueOf(prescriptedDrug.getDrugId()));
                System.out.print("Visit id: " + String.valueOf(prescriptedDrug.getVisitId()));
                System.out.print("Amount: " + String.valueOf(value.intValue()));

                PrescriptedDrugsPK prescriptedDrugsPK = new PrescriptedDrugsPK();
                prescriptedDrugsPK.setDrugId(key.getDrugId());
                prescriptedDrugsPK.setVisitId(visit.getVisitId());

                entityManager.persist(prescriptedDrug);
            });

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

        controller.setPrescriptionNumber(presciptionNumber);
        this.OnCancel(event);
    }

    public void setVisit(Visits visit) {
        this.visit = visit;
    }

    public void setVisitDetailController(VisitsDetailsFxController controller) {
        this.controller = controller;
    }
}
