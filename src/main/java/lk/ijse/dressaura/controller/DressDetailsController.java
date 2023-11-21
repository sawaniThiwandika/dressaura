package lk.ijse.dressaura.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.DressDto;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dressaura.dto.RentDetailsDto;
import lk.ijse.dressaura.model.DressModel;
import lk.ijse.dressaura.model.RentDetailsModel;

import javax.management.Notification;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class DressDetailsController {
    @FXML
    private Button deleteBtn;

    @FXML
    private AnchorPane image;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelId;

    @FXML
    private Label labelName;

    @FXML
    private Label labelRentPrice;

    @FXML
    private Label labelSize;

    @FXML
    private Button okBtn;
    @FXML
    private Button updateBtn;
    public DressDto dto;
    @FXML
    void deleteBtnOnAction(ActionEvent event) throws SQLException {
        DressModel dressModel=new DressModel();

        boolean ongoing = checkOngoingRent();
        if(ongoing){
            new Alert(Alert.AlertType.ERROR,"Can not delete").show();
        }
        else{

            boolean isDeleted = dressModel.deleteDress(dto);
            if(isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"Successfully Deleted").show();
            }

        }


    }

    private boolean checkOngoingRent() throws SQLException {
        RentDetailsModel rentDetailsModel=new RentDetailsModel();
        List<RentDetailsDto> allRentals = rentDetailsModel.getAllRentals();
        for (int i=0;i<allRentals.size();i++){
            if(allRentals.get(i).getDress_id().equals(dto.getDressId())){
                return true;

            }
        }
        return false;
    }

    @FXML
    void updateOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/add_dress_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        AddDressFormController controller = fxmlLoader.getController();
        controller.initialize(dto);
        Stage stage = new Stage();
        stage.setTitle("Update customer");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(true);
        stage.show();


    }
    @FXML
    void okBtnOnAction(ActionEvent event) {
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }

    public void setValues(DressDto dressDetails) {
        dto=dressDetails;
        labelId.setText(dressDetails.getDressId());
        labelName.setText(dressDetails.getName());
        labelSize.setText(dressDetails.getSize());
        labelDate.setText(String.valueOf(dressDetails.getDate()));
        labelRentPrice.setText(String.valueOf(dressDetails.getRentPrice()));
    }
}
