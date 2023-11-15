package lk.ijse.dressaura.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.SupplierDto;
import lk.ijse.dressaura.model.SupplierModel;

public class AddSupplierFormController {

    @FXML
    private Button addBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelSupId;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;
    SupplierModel supModel=new SupplierModel();
    public void initialize() throws SQLException {
        generateNextSupplierId();
        setDate();

    }

    private void generateNextSupplierId() {
        try {
            String supplierId = supModel.generateNextSupplierId();
            labelSupId.setText(supplierId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setDate() {
        labelDate.setText(String.valueOf(LocalDate.now()));
    }

    public void addButtonOnAction(javafx.event.ActionEvent actionEvent) {
        String id=labelSupId.getText();
        String name = txtName.getText();
        String contact=txtContact.getText();
        String email=txtEmail.getText();

        SupplierDto dto=new SupplierDto(id,name,contact,email);
        try {
            boolean isSaved = supModel.saveSupplier(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "supplier saved!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }


    }

    private void clearFields() {
        txtName.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        labelSupId.setText("");
    }

    public void cancelButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();

    }
}
