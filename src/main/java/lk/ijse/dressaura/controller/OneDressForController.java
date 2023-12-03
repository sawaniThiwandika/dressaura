package lk.ijse.dressaura.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dressaura.dto.DressDto;


public class OneDressForController {
    @FXML
    private ImageView imageVIiew;


    @FXML
        private Label avalibility;

        @FXML
        private Label dressID;

        @FXML
        private Label dressName;
        @FXML
        private Button more;
        @FXML
        private Label sizeLabel;
        @FXML
        private Button rent;
        private DressDto dto;

    @FXML
    void MoreButtonOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/dress_details_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        DressDetailsController controller = fxmlLoader.getController();
        controller.setValues(getDressDetails());
        Stage stage = new Stage();
        stage.setTitle("Dress details");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
    public void setDressDetails(DressDto dressDto){
       dto=dressDto;
    }
    public DressDto getDressDetails(){
       return dto;
    }
    @FXML
    void rentButtonOnAction(ActionEvent event) throws IOException {

        URL resource = this.getClass().getResource("/view/add_rental_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        AddRentalFormController controller = fxmlLoader.getController();
        controller.setDetails(getDressDetails());
        Stage stage = new Stage();
        stage = new Stage();
        stage.setTitle("Dress details");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }


        public Label getName() {
        return dressName;
        }
        public Label getDressAvelibity() {
            return avalibility;
        }
        public Label getDressID() {
            return dressID;
        }
        public ImageView getDressImage() {
            return imageVIiew;
        }

        public Label getDressSize() {
            return sizeLabel;
        }
    }
