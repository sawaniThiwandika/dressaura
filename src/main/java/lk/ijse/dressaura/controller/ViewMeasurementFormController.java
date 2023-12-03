package lk.ijse.dressaura.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.OrderDetailsDto;
import lk.ijse.dressaura.dto.OrderDto;
import lk.ijse.dressaura.dto.tm.ViewMaterialsTm;
import lk.ijse.dressaura.model.MaterialModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import com.jfoenix.controls.JFXButton;
import lk.ijse.dressaura.model.OrderModel;

public class ViewMeasurementFormController {
    @FXML
    private JFXButton newDesign;
        @FXML
        private JFXButton viewDesignButton;
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
        OrderDto orderDto=new OrderDto();

        String photoPath;
    @FXML
    void viewDesignButtonOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/view_design_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        ViewDesignForm controller = fxmlLoader.getController();
        System.out.println("abc"+orderDto.getDesign());
        controller.setValues(orderDto.getDesign());

        Stage stage = new Stage();
        stage.setTitle("Dress details");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();


    }

    @FXML
        void updateButtonOnAction(ActionEvent event) throws SQLException {
        String labelOrderIdText = labelOrderId.getText();
        String waistText = txtWaist.getText();
        String inseamText = txtInseam.getText();
        String shoulderText = txtShoulder.getText();
        String neckText = txtNeck.getText();
        String hipsText = txtHips.getText();
        String bustText = txtBust.getText();
        String text = txtDescription.getText();
        String design=photoPath;

        OrderDto dto =new OrderDto(labelOrderIdText,waistText,inseamText,shoulderText,neckText,hipsText,bustText,text,design);
        OrderModel model=new OrderModel();
        boolean b = model.updateOrderMeasurements(dto);
        if(b){
            new Alert(Alert.AlertType.CONFIRMATION,"Success").show();
        }


    }

        @FXML
        void cancelButtonOnAction(ActionEvent event) {
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();

        }

    public void initialize(OrderDto dto, ArrayList<OrderDetailsDto> orderDetails) throws SQLException {
         this.orderDto=dto;
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
        photoPath= dto.getDesign();

        for (OrderDetailsDto detailsDto: orderDetails){

            obList.add(new ViewMaterialsTm(detailsDto.getMaterialId(),materialModel.getName(detailsDto.getMaterialId()),
                    detailsDto.getAmount()));
        }
        materialTable.setItems(obList);






    }

    @FXML
    void newDesignButtonOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            photoPath = selectedFile.getAbsolutePath();

        }
    }
}
