package lk.ijse.dressaura.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.*;
import lk.ijse.dressaura.model.MaterialModel;
import lk.ijse.dressaura.model.PaymentModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

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
        //setValues(dto);
    }
    public void initialize(MaterialDto dto) throws SQLException {
        genarateMaterial();
        setDate();
        setcomboBoxValues();
        setValues(dto);
    }
    public  void setValues(MaterialDto dto) throws SQLException {
        labelMaterialId.setText(dto.getMaterialId());
        txtMaterial.setText(dto.getName());
        txtSellPrice.setText(String.valueOf(dto.getUnitPrice()));
        if(dto.getMaterialId().isEmpty()){
          genarateMaterial();
          setcomboBoxValues();

        }


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
        String m_id =materialModel.generateNextId();
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
        boolean isValid=validateMaterial();

        if(isValid){
           // new Alert(Alert.AlertType.ERROR,"Please fill all above fields").show();
            LocalDate date = LocalDate.parse(labelDate.getText());
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

    private boolean validateMaterial() {

        if(comboSupplierId.getValue()==null){
            new Alert(Alert.AlertType.ERROR,"supplier details empty").show();
            return false;
        }
        if(txtMaterial.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Material name empty").show();
            return false;
        }

        boolean matchName = Pattern.matches("^[A-Za-z]+(?:[ '-][A-Za-z]+)*$",txtMaterial.getText());
        if(!matchName){
            new Alert(Alert.AlertType.ERROR,"Invalid material name").show();
            return  false;
        }
        if(txtSellPrice.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Selling price empty").show();
            return false;
        }
        boolean matchSPrice = Pattern.matches("^\\d+(\\.\\d{1,2})?$",txtSellPrice.getText());
        if(!matchSPrice){
            new Alert(Alert.AlertType.ERROR,"Invalid selling price").show();
            return  false;
        }
        if(txtPrice.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"price empty").show();
            return false;
        }
        boolean matchPrice = Pattern.matches("^\\d+(\\.\\d{1,2})?$",txtPrice.getText());
        if(!matchPrice){
            new Alert(Alert.AlertType.ERROR,"Invalid  price").show();
            return  false;
        }
        if(txtAmount.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"amount empty").show();
            return false;
        }
        boolean matchAmount = Pattern.matches("^\\d+(\\.\\d{1,2})?$",txtAmount.getText());
        if(!matchAmount){
            new Alert(Alert.AlertType.ERROR,"Invalid amount").show();
            return  false;
        }
        if(!checkbtn.isSelected()){
            new Alert(Alert.AlertType.ERROR,"Empty fields... please check in").show();
            return  false;
        }

        return true;

    }

    @FXML
    void checkboxOnAction(ActionEvent event) {
        Double price = Double.valueOf(txtPrice.getText());
        Double amount = Double.valueOf(txtAmount.getText());

        Double total=price*amount;
        labelCost.setText(String.valueOf(total));


    }



}
