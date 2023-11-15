package lk.ijse.dressaura.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.MaterialDto;
import lk.ijse.dressaura.dto.PaymentDto;
import lk.ijse.dressaura.dto.SupplierDetailsDto;
import lk.ijse.dressaura.dto.SupplierDto;
import lk.ijse.dressaura.model.MaterialModel;
import lk.ijse.dressaura.model.PaymentModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AddMaterialFormController {

    @FXML
    private Button add;

    @FXML
    private Button cancel;

    @FXML
    private Label labelCost;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelMaterialId;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtMaterial;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtSellPrice;

    @FXML
    private Label labelSupplierName;

    @FXML
    private CheckBox checkbtn;
    @FXML
    private ComboBox<String> lebalSupplierId;

    @FXML
    private ComboBox<String> comboSupplierId;
    private MaterialModel model=new MaterialModel();
    public void initialize() throws SQLException {
        genarateMaterial();
        setDate();
        setcomboBoxValues();
    }

    private void setcomboBoxValues() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<SupplierDto> supplierDtos =model.getSupplierTableValues();

            for (SupplierDto dto : supplierDtos) {
                obList.add(dto.getSupId());
            }
            comboSupplierId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void comboSupplierOnAction(ActionEvent event) throws SQLException {
        String supId = comboSupplierId.getValue();
        labelSupplierName.setText(model.searchSupplier(supId).getName());

    }

    private void genarateMaterial() throws SQLException {
        MaterialModel materialModel=new MaterialModel();
        String m_id =MaterialModel.generateNextId();
        labelMaterialId.setText(m_id);


    }
    private void setDate() {
        labelDate.setText(String.valueOf(LocalDate.now()));
    }


    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    @FXML
    void addButtonOnAction(ActionEvent event) throws SQLException {

        if(!checkbtn.isSelected() ||comboSupplierId.getValue().isEmpty()||labelCost.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please fill all above fields").show();
        }
        else{ LocalDate date = LocalDate.parse(labelDate.getText());
            String m_id = labelMaterialId.getText();
            String value = (String) comboSupplierId.getValue();
            String supName = labelSupplierName.getText();
            Double amount = Double.valueOf(txtAmount.getText());
            String m_name = txtMaterial.getText();
            Double price = Double.valueOf(txtPrice.getText());
            Double sellPrice  = Double.valueOf(txtSellPrice.getText());
            Double cost= Double.valueOf(labelCost.getText());

            MaterialDto dtoM= new MaterialDto(m_id,amount,sellPrice,m_name);
            PaymentModel modelp=new PaymentModel();
            String pay_id = modelp.generateNextId();
            PaymentDto dtoP=new PaymentDto(pay_id,date,cost);
            SupplierDetailsDto dtoSD=new SupplierDetailsDto(value,m_id,amount,price);

            boolean isSaved = model.saveMaterial(dtoM, dtoP, dtoSD);
            if(isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"succcessfully saved").show();
            }

        }

    }
    @FXML
    void checkboxOnAction(ActionEvent event) {
        Double price = Double.valueOf(txtPrice.getText());
        Double amount = Double.valueOf(txtAmount.getText());

        Double total=price*amount;
        labelCost.setText(String.valueOf(total));

    }



}
