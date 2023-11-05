package lk.ijse.dressaura.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AddCustomerFormController {
    @FXML
    private Button add;

    @FXML
    private Button cancel;
    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void addButtonOnAction(ActionEvent event) {

    }
}
