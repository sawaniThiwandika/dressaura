package lk.ijse.dressaura.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dressaura.dto.tm.PaymentTm;
import lk.ijse.dressaura.model.PaymentModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PaymentFormController {

    @FXML
    private TableColumn<?, ?> colActionCost;

    @FXML
    private TableColumn<?, ?> colActionIncome;

    @FXML
    private TableColumn<?, ?> colAmountCost;

    @FXML
    private TableColumn<?, ?> colCusIdIncome;

    @FXML
    private TableColumn<?, ?> colCusNmeIncome;

    @FXML
    private TableColumn<?, ?> colDateCost;



    @FXML
    private TableColumn<?, ?> colNumCost;

    @FXML
    private TableColumn<?, ?> colNumIncome;

    @FXML
    private TableColumn<?, ?> colSupIdCost;

    @FXML
    private TableColumn<?, ?> colPayIdCost;

    @FXML
    private TableColumn<?, ?> colPayIdCus;

    @FXML
    private TableColumn<?, ?> colSupNameCost;

    @FXML
    private TableColumn<?, ?> colTypeAmount;

    @FXML
    private TableColumn<?, ?> colTypeIncome;

    @FXML
    private TableColumn<?, ?> coldateIncome;

    @FXML
    private Tab costTab;

    @FXML
    private TableView<PaymentTm> cusPaymentTable;

    @FXML
    private Label labelCost;

    @FXML
    private Label labelIncome;

    @FXML
    private Label labelProfit;

    @FXML
    private TableView<PaymentTm> materialPaymentTable;

    @FXML
    private AnchorPane tabCost;

    @FXML
    private AnchorPane tabIncome;

    @FXML
    private TabPane tabPane;
    public void initialize() throws SQLException, IOException {

        loadAllIncomePayments();
        loadAllCostPayments();
        setCellValueFactory();
        calculateTotal();
        calculateCost();
        calculateProfit();

    }

    private void loadAllCostPayments() throws SQLException {
        PaymentModel paymodel=new PaymentModel();
        List<PaymentTm>paymentDetails =paymodel.getCostPaymentDetails();
        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();
        for(PaymentTm paymentTm:paymentDetails){
            obList.add(paymentTm);
        }
        materialPaymentTable.setItems(obList);

    }

    private void setCellValueFactory() {
        colNumIncome.setCellValueFactory(new PropertyValueFactory<>("number_income"));
        colNumCost.setCellValueFactory(new PropertyValueFactory<>("number_cost"));
        colCusIdIncome.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colPayIdCus.setCellValueFactory(new PropertyValueFactory<>("payIdIncome"));
        colPayIdCost.setCellValueFactory(new PropertyValueFactory<>("payIdCost"));
       colSupIdCost.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colCusNmeIncome.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        colSupNameCost.setCellValueFactory(new PropertyValueFactory<>("supName"));
        coldateIncome.setCellValueFactory(new PropertyValueFactory<>("dateIncome"));
        colDateCost.setCellValueFactory(new PropertyValueFactory<>("dateCost"));
        colTypeIncome.setCellValueFactory(new PropertyValueFactory<>("type"));
        //colMtarialIdCost.setCellValueFactory(new PropertyValueFactory<>("materialId"));
        colAmountCost.setCellValueFactory(new PropertyValueFactory<>("amountCost"));
        colTypeAmount.setCellValueFactory(new PropertyValueFactory<>("amountIncome"));
        colActionCost.setCellValueFactory(new PropertyValueFactory<>("updatePC_button"));
        colActionIncome.setCellValueFactory(new PropertyValueFactory<>("updatePI_button"));


    }

    private void loadAllIncomePayments() throws SQLException {
        PaymentModel paymodel=new PaymentModel();
        List<PaymentTm>paymentDetails =paymodel.getIncomePaymentDetails();
        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();
        for(PaymentTm paymentTm:paymentDetails){
           obList.add(paymentTm);
        }
        cusPaymentTable.setItems(obList);

    }
    private void calculateTotal() throws SQLException {
        double total = 0;
        for (int i = 0; i < cusPaymentTable.getItems().size(); i++) {
            total = (double)colTypeAmount.getCellData(i)+total;
        }


        labelIncome.setText(String.valueOf(total));

    }
    private void calculateCost() throws SQLException {
        double cost = 0;
        for (int i = 0; i < materialPaymentTable.getItems().size(); i++) {
           cost += (double) colAmountCost.getCellData(i);
        }


        labelCost.setText(String.valueOf(cost));

    }
    private void calculateProfit() throws SQLException {
        Double cost = Double.valueOf(labelCost.getText());
        Double income = Double.valueOf(labelIncome.getText());
        Double profit=income-cost;

        labelProfit.setText(String.valueOf(profit));

    }


}

