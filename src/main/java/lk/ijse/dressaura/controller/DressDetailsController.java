package lk.ijse.dressaura.controller;

import javafx.stage.Stage;
import lk.ijse.dressaura.dto.DressDto;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DressDetailsController {

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
    void okBtnOnAction(ActionEvent event) {
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }

    public void setValues(DressDto dressDetails) {
        labelId.setText(dressDetails.getDressId());
        labelName.setText(dressDetails.getName());
        labelSize.setText(dressDetails.getSize());
        labelDate.setText(String.valueOf(dressDetails.getDate()));
        labelRentPrice.setText(String.valueOf(dressDetails.getRentPrice()));
    }
}
