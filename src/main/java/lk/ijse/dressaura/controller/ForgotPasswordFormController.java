package lk.ijse.dressaura.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.UserDto;
import lk.ijse.dressaura.model.UserModel;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
public class ForgotPasswordFormController {
    @FXML
    private TextField code;

    @FXML
    private Button confirmCodeButton;

    @FXML
    private Button confirmEmail;
    @FXML
    private TextField userName;
    @FXML
    private TextField txtEmail;




    @FXML
    private AnchorPane forgetPasswordForm;



    @FXML
    private TextField txtUserName;

    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {

        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) forgetPasswordForm.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("login");
        stage.centerOnScreen();
    }
    @FXML
    void confirmEmailButtonOnAction(ActionEvent event) throws SQLException {
        String userName=txtUserName.getText();
        String useremail=txtEmail.getText();
        var loginDto=new UserDto();
        loginDto.setUserName(userName);
        loginDto.setEmail(useremail);
        boolean isMatched=UserModel.checkUsernameEmail(loginDto);
        if(isMatched){
            new Alert(Alert.AlertType.CONFIRMATION, "check your email and enter the code!").show();
        }
        else {
            new Alert(Alert.AlertType.CONFIRMATION, " try again!").show();
        }
    }

    @FXML
    void confirmButtonOnAction(ActionEvent event) throws IOException {
        String recoveryCode=code.getText();
        boolean isMatched=UserModel.checkRecovaryCode(recoveryCode);
        if(isMatched==false){
            new Alert(Alert.AlertType.CONFIRMATION, "wrong code try again!").show();
        }
        else
        {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/main_form.fxml"));
            Scene scene = new Scene(anchorPane);
            Stage stage = (Stage) forgetPasswordForm.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("main page");
            stage.centerOnScreen();
        }

    }

    }


