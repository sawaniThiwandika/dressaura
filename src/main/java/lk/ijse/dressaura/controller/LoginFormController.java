package lk.ijse.dressaura.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import lk.ijse.dressaura.dto.UserDto;
import lk.ijse.dressaura.model.UserModel;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;


import javafx.scene.control.TextField;

public class LoginFormController {
    @FXML
    private AnchorPane loginpage;
    @FXML
    private Button login;
    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;
    static UserDto loginDto=new UserDto();
    @FXML
    void txtPasswordOnAction(ActionEvent event) throws SQLException {
        System.out.println("username");
        String userName=txtUsername.getText();

        loginDto.setUserName(userName);
        boolean isMatchedUsername=UserModel.checkUsername(loginDto);
      if(isMatchedUsername==false){
          new Alert(Alert.AlertType.CONFIRMATION, "wrong username try again!").show();
     }

    }
    @FXML
    public void loginButtonOnAction(ActionEvent actionEvent) throws IOException, SQLException {
        String userName=txtUsername.getText();
        String password=txtPassword.getText();
        //var loginDto=new UserDto(userName,password);
        loginDto.setPassword(password);
        loginDto.setUserName(userName);

        boolean isMatched=UserModel.checkCreditinal(loginDto);
        if(isMatched==false){
            new Alert(Alert.AlertType.CONFIRMATION, "wrong password try again!").show();
        }
        else
             {
             AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/main_form.fxml"));
             Scene scene = new Scene(anchorPane);
             Stage stage = (Stage) loginpage.getScene().getWindow();
             stage.setScene(scene);
             stage.setTitle("dashboard");
             stage.centerOnScreen();
             }

            }


    public void forgetPasswordOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/forgot_Password_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) loginpage.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("forgot password page");
        stage.centerOnScreen();
    }
}
