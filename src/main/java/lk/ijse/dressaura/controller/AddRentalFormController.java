package lk.ijse.dressaura.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import java.awt.*;

import java.io.IOException;
import java.net.URL;



public class AddRentalFormController {
    @FXML
    private Button add;
    @FXML
    private Button cancel;
    @FXML
    private AnchorPane newrental;

    @FXML
    void cancelButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    @FXML
    void addButtonOnAction(ActionEvent event) {

    }
}
