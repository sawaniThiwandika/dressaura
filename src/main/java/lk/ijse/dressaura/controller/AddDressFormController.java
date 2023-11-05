package lk.ijse.dressaura.controller;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
public class AddDressFormController {
    @FXML
    private Button cancel;
    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void addButtonOnAction(ActionEvent actionEvent) {

    }
}
