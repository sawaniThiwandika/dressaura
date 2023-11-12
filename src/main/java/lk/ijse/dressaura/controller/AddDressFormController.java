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
    private ComboBox<String> combosize;

    @FXML
    private TextField txtname;

    @FXML
    private TextField txtprice;

    @FXML
    private TextField txttype;
    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        String name= txtname.getText();
        String  type= txttype.getText();
        String dressId=dressIdLabel.getText();
        LocalDate date = LocalDate.parse(labeldate.getText());
        String userName= LoginFormController.loginDto.getUserName();
        System.out.println("userName"+userName);
        double rentPrice=Double.valueOf(txtprice.getText());
        boolean avelability=true;
        String size=combosize.getValue();
        var dto= new DressDto(userName,name,dressId, size,rentPrice,avelability,null, type,date);
        try {
            boolean isSuccess = DressModel.addDress(dto);
            if (isSuccess) {
                new Alert(Alert.AlertType.CONFIRMATION, "Order Success!").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void initialize() {
        setValuesCombobox();
        generateNextDressId();
        setDate();
    }

    private void setValuesCombobox() {
        ObservableList<String>  sizes = FXCollections.observableArrayList("S", "M", "L", "XL", "XXL", "XXXL", "free size");
        combosize.setItems(sizes);
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
}
