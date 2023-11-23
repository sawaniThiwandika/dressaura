package lk.ijse.dressaura.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.UserDto;
import lk.ijse.dressaura.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AddUserFormController {

    @FXML
    private TextField txtConfirmPassword;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    private AnchorPane addUser;
    @FXML
    void addButtonOnAction(ActionEvent event) throws SQLException {
        String userName = txtUserName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String confirm=txtConfirmPassword.getText();


        if(txtUserName.getText().isEmpty()||txtConfirmPassword.getText().isEmpty()||txtPassword.getText().isEmpty()||
        txtEmail.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Empty fields").show();

        }

        else {
            boolean validateUser = validateUser();
            if (validateUser) {
                boolean equals = txtPassword.getText().equals(txtConfirmPassword.getText());
                if (equals) {
                    UserDto newUser=new UserDto(txtUserName.getText(),txtPassword.getText(),txtEmail.getText());
                    UserModel userModel=new UserModel();
                    boolean isSaved = userModel.saveUser(newUser);
                    if (isSaved){
                        new Alert(Alert.AlertType.CONFIRMATION, "success").show();
                    }


                } else {
                    new Alert(Alert.AlertType.ERROR, "password invalid...").show();
                }
            }
        }

    }

    private boolean validateUser() {

        boolean matchEmail= Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])" +
                "|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:" +
                "[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",txtEmail.getText());
        if(!matchEmail){
            new Alert(Alert.AlertType.ERROR,"Invalid email").show();
            return  false;
        }
        return matchEmail;
    }

    @FXML
    void backButtonOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) addUser.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("login");
        stage.centerOnScreen();

    }

}
