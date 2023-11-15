package lk.ijse.dressaura.controller;

import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.CustomerDto;
import lk.ijse.dressaura.model.CustomerModel;

public class UpdateCustomerFormController {

    @FXML
    private Button cancel;

    @FXML
    private Button ok;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Button update;

    private CustomerModel customerModel = new CustomerModel();
    public void initialize(CustomerDto customerDto) throws SQLException, IOException {
        txtEmail.setText(customerDto.getEmail());
        txtName.setText(customerDto.getName());
        txtContact.setText(customerDto.getContact());
        txtAddress.setText(customerDto.getAddress());
        txtId.setText(customerDto.getCusId());


    }

    public void updateButtonOnAction(javafx.event.ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtContact.getText();
        String email = txtEmail.getText();

        var dto = new CustomerDto(name, email,address,tel,id);

        try {
            boolean isSaved = customerModel.updateCustomer(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {

        txtId.setText("");
        txtAddress.setText("");
        txtName.setText("");
        txtContact.setText("");
        txtEmail.setText("");

    }

    public void cancelButtonOnAction(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();

    }

    @FXML
    void okButtonOnAction(ActionEvent event) throws SQLException {
        String cusId = txtId.getText();
        ArrayList<CustomerDto> customer = customerModel.getCustomer(cusId);
        int i=0;
        while (i<customer.size()) {

            txtEmail.setText(customer.get(i).getEmail());
            txtContact.setText(customer.get(i).getContact());
            txtAddress.setText(customer.get(i).getAddress());
            txtName.setText(customer.get(i).getName());
            i++;
        }

    }



}



