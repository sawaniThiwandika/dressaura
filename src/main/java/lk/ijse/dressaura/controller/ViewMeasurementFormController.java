package lk.ijse.dressaura.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.OrderDetailsDto;
import lk.ijse.dressaura.dto.OrderDto;
import lk.ijse.dressaura.dto.tm.ViewMaterialsTm;
import lk.ijse.dressaura.model.MaterialModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class ViewMeasurementFormController {

        @FXML
        private Button updateBtn;

        @FXML
        private Button cancel;

        @FXML
        private TableColumn<?, ?> colAmount;

        @FXML
        private TableColumn<?, ?> colMaterialId;

        @FXML
        private TableColumn<?, ?> colMaterialName;

        @FXML
        private Label labelOrderId;

        @FXML
        private TableView<ViewMaterialsTm> materialTable;

        @FXML
        private TextField txtBust;

        @FXML
        private TextArea txtDescription;

        @FXML
        private TextField txtHips;

        @FXML
        private TextField txtInseam;

        @FXML
        private TextField txtNeck;

        @FXML
        private TextField txtShoulder;

        @FXML
        private TextField txtWaist;
        private ObservableList<ViewMaterialsTm> obList = FXCollections.observableArrayList();

        MaterialModel materialModel= new MaterialModel();
        @FXML
        void updateButtonOnAction(ActionEvent event) {

        }

        @FXML
        void cancelButtonOnAction(ActionEvent event) {
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();

        }

    public void initialize(OrderDto dto, ArrayList<OrderDetailsDto> orderDetails) throws SQLException {
            setValues(dto,orderDetails);
            setCellValueFactory();

    }

    private void setCellValueFactory() {
        colMaterialId.setCellValueFactory(new PropertyValueFactory<>("material_id"));
        colMaterialName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

    }

    private void setValues(OrderDto dto, ArrayList<OrderDetailsDto> orderDetails) throws SQLException {

        txtBust.setText(String.valueOf(dto.getBust()));
        txtHips.setText(String.valueOf(dto.getHips()));
        txtNeck.setText(String.valueOf(dto.getNeck()));
        txtShoulder.setText(String.valueOf(dto.getShoulder()));
        txtWaist.setText(String.valueOf(dto.getWaist()));
        txtInseam.setText(String.valueOf(dto.getInseam()));
        labelOrderId.setText(dto.getOrderId());

        for (OrderDetailsDto detailsDto: orderDetails){

            obList.add(new ViewMaterialsTm(detailsDto.getMaterialId(),materialModel.getName(detailsDto.getMaterialId()),
                    detailsDto.getAmount()));
        }
        materialTable.setItems(obList);






    }
}
