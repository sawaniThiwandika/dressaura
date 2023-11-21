package lk.ijse.dressaura.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lk.ijse.dressaura.dto.DressDto;
import lk.ijse.dressaura.model.DressModel;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class AddDressFormController {
    private DressModel dressModel = new DressModel();
    @FXML
    private Label dressIdLabel;
    @FXML
    private Label labeldate;
    @FXML
    private Button cancel;
    @FXML
    private Button addphoto;
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private ComboBox<String> combosize;

    @FXML
    private TextField txtname;

    @FXML
    private TextField txtprice;


    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        boolean isvalid = validateDressDetails();
        if(isvalid) {
            String name = txtname.getText();
            String type = comboType.getValue();
            String dressId = dressIdLabel.getText();
            LocalDate date = LocalDate.parse(labeldate.getText());
            String userName = LoginFormController.loginDto.getUserName();
            System.out.println("userName" + userName);
            double rentPrice = Double.valueOf(txtprice.getText());
            boolean avelability = true;
            String size = combosize.getValue();
            var dto = new DressDto(userName, name, dressId, size, rentPrice, avelability, null, type, date);
            try {
                boolean isSuccess = dressModel.addDress(dto);
                if (isSuccess) {
                    new Alert(Alert.AlertType.CONFIRMATION, " Success!").show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void initialize(DressDto dto) {
      dressIdLabel.setText(dto.getDressId());
      txtname.setText(dto.getName());
      combosize.setValue(dto.getSize());
      txtprice.setText(String.valueOf(dto.getRentPrice()));
      comboType.setValue(dto.getType());
      labeldate.setText(String.valueOf(dto.getDate()));
    }
    public void initialize() {
        setValuesCombobox();
        generateNextDressId();
        setDate();
        setValuesTypes();
    }

    private void setValuesCombobox() {
        ObservableList<String>  sizes = FXCollections.observableArrayList("S", "M", "L", "XL", "XXL", "XXXL",
                "free size");
        combosize.setItems(sizes);
    }
    private void setValuesTypes() {
        ObservableList<String>  types = FXCollections.observableArrayList("Frock", "Saree", "Lehenga", "Salwar Kameez", "Kurta");
        comboType.setItems(types);
    }

    private void generateNextDressId() {
        try {
            String orderId = dressModel.generateNextDressId();
            dressIdLabel.setText(orderId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    private void setDate() {
       labeldate.setText(String.valueOf(LocalDate.now()));
    }

    @FXML
    void comboTypeOnAction(ActionEvent event) {

    }

    public boolean validateDressDetails(){
        String name=txtname.getText();
        if(name.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Dress name field empty").show();
            return false;
        }
        if(comboType.getValue() == null){
            new Alert(Alert.AlertType.ERROR,"Dress type field empty").show();
            return false;
        }
        if(combosize.getValue() == null){
            new Alert(Alert.AlertType.ERROR,"Dress Size field empty").show();
            return false;
        }

        if(txtprice.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Dress rent Price field empty").show();
            return false;
        }
        boolean matchName = Pattern.matches("^[A-Za-z]+(?:[ '-][A-Za-z]+)*$",name);
        if(!matchName){
            new Alert(Alert.AlertType.ERROR,"Invalid name").show();
            return  false;
        }
        boolean matchPrice = Pattern.matches("^\\d+(\\.\\d{1,2})?$",txtprice.getText());
        if(!matchPrice){
            new Alert(Alert.AlertType.ERROR,"Invalid price").show();
            return  false;
        }

        return true;
    }

}
