package lk.ijse.dressaura.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        boolean isValid = validateSupplier();
if(isValid) {
    String id = labelSupId.getText();
    String name = txtName.getText();
    String contact = txtContact.getText();
    String email = txtEmail.getText();

    SupplierDto dto = new SupplierDto(id, name, contact, email);
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

    }

    private boolean validateSupplier() {
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
        txtName.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        labelSupId.setText("");
    }

    public void cancelButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();

    }

    public void initialize(SupplierDto supplierDto) {
        txtEmail.setText(supplierDto.getEmail());
        txtContact.setText(supplierDto.getContact());
        txtName.setText(supplierDto.getName());
        labelSupId.setText(supplierDto.getSupId());
        addBtn.setText("Update");
    }
}
