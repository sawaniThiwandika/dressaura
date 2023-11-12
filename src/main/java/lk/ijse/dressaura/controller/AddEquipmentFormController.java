package lk.ijse.dressaura.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
public class AddEquipmentFormController {

    @FXML
    private Button cancel;

    public void cancelButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
