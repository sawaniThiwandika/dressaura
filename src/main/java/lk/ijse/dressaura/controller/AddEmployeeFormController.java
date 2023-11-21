package lk.ijse.dressaura.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lk.ijse.dressaura.dto.EmployeeDto;
import lk.ijse.dressaura.model.EmployeeModel;

public class AddEmployeeFormController {
    @FXML
    private Button cancel;
    @FXML
    private Button addBtn;


    @FXML
    private Label labelDate;

    @FXML
    private Label labelEmpId;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtJobRole;

    @FXML
    private TextField txtName;
    EmployeeModel empModel=new EmployeeModel();
    public void initialize() throws SQLException {
        generateNextEmployeeId();
        setDate();

    }

    private void setDate() {
       labelDate.setText(String.valueOf(LocalDate.now()));
    }

    private void generateNextEmployeeId() {
        try {
            String empId = empModel.generateNextEmployeeId();
            labelEmpId.setText(empId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    void addButtonOnAction(ActionEvent event) throws SQLException {

        boolean isValid = validateEmployee();
        if(isValid) {
            String empId = labelEmpId.getText();
            LocalDate date = LocalDate.parse(labelDate.getText());
            String name = txtName.getText();
            String address = txtAddress.getText();
            String contact = txtContact.getText();
            String email = txtEmail.getText();
            String jobRole = txtJobRole.getText();
            String userName = LoginFormController.loginDto.getUserName();
            EmployeeDto dto = new EmployeeDto(name, empId, email, address, contact, userName, jobRole);
            boolean isSaved = empModel.addEmployee(dto);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "successfully saved").show();
            }


        }
    }

    private boolean validateEmployee() {
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
            new Alert(Alert.AlertType.ERROR,"Invalid Employee name").show();
            return  false;
        }



        String address = txtAddress.getText();
        if(txtAddress.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Employee address field empty").show();
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

    @FXML
    public void cancelButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }


}
