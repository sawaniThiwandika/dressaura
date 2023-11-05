package lk.ijse.dressaura.controller;

import javafx.fxml.FXML;

import java.awt.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AddOrderFormController {
    @FXML
    private Button cancel;

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

}
