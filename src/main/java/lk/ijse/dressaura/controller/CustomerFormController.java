package lk.ijse.dressaura.controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class CustomerFormController {
    @FXML
    private Button add;

    @FXML
    void addButtonOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/add_customer_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Add customer");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
}
