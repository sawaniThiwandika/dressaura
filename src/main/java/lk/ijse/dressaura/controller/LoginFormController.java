package lk.ijse.dressaura.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.scene.control.Button;

import java.io.IOException;

public class LoginFormController {
    @FXML
    private AnchorPane loginpage;
    @FXML
    private Button login;
    @FXML
    public void loginButtonOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/main_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) loginpage.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("dashboard");
        stage.centerOnScreen();
    }
}
