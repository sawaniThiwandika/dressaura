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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
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

        boolean isvalid = validationCustomer();
        if(isvalid) {

            String id = custId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String tel = txtContact.getText();
            String email = txtEmail.getText();
            //Integer.valueOf(txtContact.getText());


            var dto = new CustomerDto(name, email, address, tel, id);

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
    }

    private boolean validationCustomer() {
        String name = txtName.getText();
        if(txtName.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Customer name field empty").show();
            return false;

        }

       Pattern compile = Pattern.compile("^[A-Za-z]+(?:[ '-][A-Za-z]+)*$");
        Matcher matcher = compile.matcher(txtName.getText());
        boolean matchName = matcher.matches();
        System.out.println(matchName);
       if(!matchName){
           new Alert(Alert.AlertType.ERROR,"Invalid customer name").show();
           return  false;
       }



        String address = txtAddress.getText();
        if(txtAddress.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Customer address field empty").show();
            return false;

        }

        boolean matchAddress = Pattern.matches("^[0-9A-Za-z\\s.,#-]+$",address);
        if(!matchAddress){
            new Alert(Alert.AlertType.ERROR,"Invalid address").show();
            return  false;
        }
        String email = txtEmail.getText();
        if(email.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Email address field empty").show();
            return false;

        }
        boolean matchEmail= Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])" +
                "|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:" +
                "[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",email);
        if(!matchEmail){
            new Alert(Alert.AlertType.ERROR,"Invalid email").show();
            return  false;
        }

        String contact = txtContact.getText();
        if(contact.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"contact field empty").show();
            return false;

        }
        boolean matchContact= Pattern.matches("^0[1-9]\\d{8}$",contact);
        if(!matchContact){
            new Alert(Alert.AlertType.ERROR,"Invalid contact number").show();
            return  false;
        }


     return true;

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
