package lk.ijse.dressaura.controller;

import javafx.fxml.FXML;

import java.awt.*;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AddOrderFormController {
    @FXML
    private Label Labeltotal;

    @FXML
    private Button addBtn;

    @FXML
    private Button btnAddDesign;

    @FXML
    private Button cancel;

    @FXML
    private CheckBox chechBoxaddMeterial;

    @FXML
    private CheckBox checkBoxTable;

    @FXML
    private ComboBox<?> comboMaterialCodes;

    @FXML
    private Label labelCusName;

    @FXML
    private TextField labelCustomerId;

    @FXML
    private Label labelMaterialCost;

    @FXML
    private Label labelOrderId;

    @FXML
    private TableView<?> materialTable;

    @FXML
    private TextField tailorFees;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtBust;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtHips;

    @FXML
    private TextField txtInseam;

    @FXML
    private TextField txtNeck;

    @FXML
    private TextField txtShoulder;

    @FXML
    private TextField txtWaist;


    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    @FXML
    void AddButtonOnAction(ActionEvent event) {

    }

}
