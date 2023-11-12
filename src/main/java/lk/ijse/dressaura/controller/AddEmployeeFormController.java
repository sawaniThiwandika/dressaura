package lk.ijse.dressaura.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Button;
public class AddEmployeeFormController {
    @FXML
    private Button cancel;

    @FXML
    public void cancelButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }


}
