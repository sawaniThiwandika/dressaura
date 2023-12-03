package lk.ijse.dressaura.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.EquipmentDto;
import lk.ijse.dressaura.model.EquipmentModel;

import java.sql.SQLException;
import java.time.LocalDate;

public class AddEquipmentFormController {

    @FXML
    private Button addBtn;
    @FXML
    private Button cancel;


    @FXML
    private Label date;

    @FXML
    private Label equipmentId;

    @FXML
    private Label title;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;
    EquipmentModel equipmentModel=new EquipmentModel();
    public void initialize() throws SQLException {
        generateNextId();
        setDate();
    }

    private void generateNextId() throws SQLException {
        String orderId =equipmentModel .generateNextId();
       equipmentId.setText(orderId);
    }
    private void setDate() {
        date.setText(String.valueOf(LocalDate.now()));
    }
    public void cancelButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    @FXML
    void addButtonOnAction(ActionEvent event) throws SQLException {
        String id = equipmentId.getText();
        LocalDate addDate = LocalDate.parse(date.getText());
        String name = txtName.getText();
        String descriptionText = txtDescription.getText();
        double priceText = Double.valueOf(txtPrice.getText());
        String userName = LoginFormController.loginDto.getUserName();

        EquipmentDto dto= new EquipmentDto(id,userName,name,priceText,addDate,descriptionText);
        boolean isSaved = equipmentModel.saveEquipment(dto);
        if (isSaved){
            new Alert(Alert.AlertType.CONFIRMATION, "Successfully Saved!").show();
        }


    }


    public void initialize(EquipmentDto equipmentDto) {
        equipmentId.setText(equipmentDto.getEquipmentId());
        txtPrice.setText(String.valueOf(equipmentDto.getPrice()));
        txtName.setText(equipmentDto.getName());
        txtDescription.setText(equipmentDto.getDescription());
        addBtn.setText("Update");
        title.setText("Update Equipment");
        date.setText(String.valueOf(equipmentDto.getDate()));


    }
}
