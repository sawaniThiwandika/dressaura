package lk.ijse.dressaura.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class OneDressForController {

    @FXML
    private Button more;

    @FXML
    private Button rent;
    @FXML
    void MoreButtonOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/dress_details_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Dress details");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
    @FXML
    void rentButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage)rent.getScene().getWindow();
        stage.close();
        URL resource = this.getClass().getResource("/view/add_rental_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        Stage stage1 = new Stage();
        stage1.setTitle("Dress details");
        stage1.setScene(new Scene(load));
        stage1.centerOnScreen();
        stage1.initModality(Modality.APPLICATION_MODAL);
        stage1.setResizable(false);
        stage1.show();
    }

}
