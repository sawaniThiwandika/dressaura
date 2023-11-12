package lk.ijse.dressaura.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lk.ijse.dressaura.dto.CustomerDto;
import lk.ijse.dressaura.model.CustomerModel;

import java.sql.SQLException;
import java.time.LocalDate;

public class AddCustomerFormController {
    @FXML
    private Button add;

    @FXML
    private Button cancel;

    @FXML
    private Label custId;

    @FXML
    private Label dateLabel;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;
    private CustomerModel cusModel = new CustomerModel();

    public void initialize() throws SQLException {
        generateNextCustomerId();
        setDate();

    }
    @FXML
    void addButtonOnAction(ActionEvent event) {
        String id = custId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtContact.getText();
        String email = txtEmail.getText();
        Integer.valueOf(txtContact.getText());

        var dto = new CustomerDto(name, email,address,tel,id);

        try {
            boolean isSaved = cusModel.saveCustomer(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer saved!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        custId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
    }
    private void generateNextCustomerId() {
        try {
            String customerId = cusModel.generateNextCustomerId();
            custId.setText(customerId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    private void setDate() {
        dateLabel.setText(String.valueOf(LocalDate.now()));
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }


}
