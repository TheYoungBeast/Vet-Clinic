package app;

import entity.Drugs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import javax.persistence.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class PrescribePrescriptionFxController implements Initializable
{
    @FXML private AnchorPane anchorPane;
    @FXML private TextField prescriptionNo;

    @FXML private TableView<Drugs> drugsTableView;
    @FXML private TableView<Drugs> prescriptedDrugsTableView;

    @FXML private TableColumn<Drugs, String> prescriptedDrugCol;
    @FXML private TableColumn<Drugs, String> prescriptedAmountCol;

    @FXML private TableColumn<Drugs, String> drugNameCol;
    @FXML private TableColumn<Drugs, String> dosageCol;
    @FXML private TableColumn<Drugs, Double> priceCol;

    @FXML private TextField searchFilter;

    private ObservableList<Drugs> drugsObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        drugNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dosageCol.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        EntityManager entityManager = EntityManagerFacade.createEntityManager();
        long id = 0;

        try {
            entityManager.getTransaction().begin();

            Query nativeQuery = entityManager.createNativeQuery("SELECT SBD_ST_PS6_4.PRESCRIPTION_ID_SEQUENCE.nextval as id FROM DUAL");
            id = ((BigDecimal) nativeQuery.getSingleResult()).longValue();

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
    }
}
