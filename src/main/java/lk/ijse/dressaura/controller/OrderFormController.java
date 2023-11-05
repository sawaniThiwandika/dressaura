package lk.ijse.dressaura.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.awt.*;

import java.io.IOException;
import java.net.URL;

public class OrderFormController {

    @FXML
    private Button add;
    @FXML
    void AddButtonOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/add_order_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Add order");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
}
