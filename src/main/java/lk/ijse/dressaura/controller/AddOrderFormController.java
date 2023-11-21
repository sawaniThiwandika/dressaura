package lk.ijse.dressaura.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;



import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.*;
import lk.ijse.dressaura.dto.tm.MaterialDressTm;
import lk.ijse.dressaura.model.CustomerModel;
import lk.ijse.dressaura.model.MaterialModel;
import lk.ijse.dressaura.model.OrderModel;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lk.ijse.dressaura.model.PaymentModel;


public class AddOrderFormController {

    @FXML
    private Label Labeltotal;

    @FXML
    private Button addBtn;

    @FXML
    private Button btnAddDesign;

    @FXML
    private Button cancel;

    @FXML
    private CheckBox checkBoxTable;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colMaterialId;

    @FXML
    private TableColumn<?, ?> colMaterialName;

    @FXML
    private ComboBox<String> comboMaterialCodes;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;
    @FXML
    private Label labelCusName;

    @FXML
    private TextField labelCustomerId;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelMaterialCost;

    @FXML
    private Label labelOrderId;

    @FXML
    private Label labelmaterialName;

    @FXML
    private TableView<MaterialDressTm> materialTable;

    @FXML
    private TextField tailorFees;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtBust;

    @FXML
    private DatePicker txtDate;

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
    @FXML
    private Button totalBtn;


    @FXML
    private ComboBox<String> comboCustomerId;
    @FXML
    private Label labelUnitPrice;
    private ObservableList<MaterialDressTm> obList = FXCollections.observableArrayList();
    public void initialize() {
        setCellValueFactory();
        generateNextOrderId();
        setDate();
        loadCustomerIds();
        loadMaterialIds();
    }
    @FXML
    void addMaterialButtonOnAction(ActionEvent event) throws SQLException {
        boolean isValid = validateMaterial();
        if(isValid) {
            String id = comboMaterialCodes.getValue();
            String name = labelmaterialName.getText();
            double qty = Double.parseDouble(txtAmount.getText());
            double unitPrice = Double.parseDouble(labelUnitPrice.getText());
            double tot = unitPrice * qty;
            Button btn = new Button("Remove");

            setRemoveBtnAction(btn);
            btn.setCursor(Cursor.HAND);


            if (!obList.isEmpty()) {
                for (int i = 0; i < materialTable.getItems().size(); i++) {
                    if (colMaterialId.getCellData(i).equals(id)) {
                        double col_qty = (double) colAmount.getCellData(i);

                        qty += col_qty;
                        tot = unitPrice * qty;

                        obList.get(i).setAmount(qty);
                        obList.get(i).setTotal(tot);

                        calculateTotal();
                        materialTable.refresh();
                        return;
                    }
                }

            }
            var materialDress = new MaterialDressTm(id, name, unitPrice, qty, tot, btn);

            obList.add(materialDress);

            materialTable.setItems(obList);
            calculateTotal();
            txtAmount.clear();
        }
    }

    private boolean validateMaterial() {
        if (comboMaterialCodes.getValue() == null){
            new Alert(Alert.AlertType.ERROR,"Empty material code ").show();
            return false;
        }
        if(txtAmount.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Empty amount").show();
            return  false;
        }
        boolean matchAmount= Pattern.matches("^\\d+(\\.\\d{1,2})?$",txtAmount.getText());
        if(!matchAmount){
            new Alert(Alert.AlertType.ERROR,"Invalid amount").show();
            return  false;
        }
       return true;
    }

    private void setRemoveBtnAction(Button btn) {

        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int focusedIndex = materialTable.getSelectionModel().getSelectedIndex();

                obList.remove(focusedIndex+1);

                materialTable.refresh();


                try {
                    calculateTotal();
                    calculateFullPayment();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }

    private void calculateTotal() throws SQLException {
        double total = 0;
        for (int i = 0; i < materialTable.getItems().size(); i++) {
            total += (double) colTotal.getCellData(i);
        }
        labelMaterialCost.setText(String.valueOf(total));

    }
    @FXML
    void totalButtonOnAction(ActionEvent event) {
        calculateFullPayment();
    }

    private void calculateFullPayment() {
        double taliorfees= Double.valueOf(tailorFees.getText());
        double materialCost= Double.parseDouble(labelMaterialCost.getText());
        double total=taliorfees+materialCost;
        Labeltotal.setText(String.valueOf(total));
    }


    private OrderModel orderModel=new OrderModel();

    public void setDate(){
        labelDate.setText(String.valueOf(LocalDate.now()));
    }

    private void loadMaterialIds() {
        MaterialModel materialModel=new MaterialModel();
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<MaterialDto> materialDtos =materialModel.loadAllMaterials();

            for (MaterialDto dto : materialDtos) {
                obList.add(dto.getMaterialId());
            }
            comboMaterialCodes.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadCustomerIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        CustomerModel customerModel=new CustomerModel();

        try {
            List<CustomerDto> idList = customerModel.getAllCustomer();

            for (CustomerDto dto : idList) {
                obList.add(dto.getCusId());
            }

           comboCustomerId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void generateNextOrderId() {
        try {
            String orderId = orderModel.generateNextOrderId();
            labelOrderId.setText(orderId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }
    @FXML
    void cmbCustomerOnAction(ActionEvent event) {
        CustomerModel modelCustomer=new CustomerModel();
        String id = comboCustomerId.getValue();
        try {
            CustomerDto customerDto = modelCustomer.searchCustomer(id);
            labelCusName.setText(customerDto.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void cmbMaterialOnAction(ActionEvent event) {
        MaterialModel materialModel=new MaterialModel();
        String id = comboMaterialCodes.getValue();
        txtAmount.requestFocus();
        try {
            MaterialDto dto = materialModel.searchMaterial(id);
            labelmaterialName.setText(dto.getName());
            labelUnitPrice.setText(String.valueOf(dto.getUnitPrice()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setCellValueFactory() {
        colMaterialId.setCellValueFactory(new PropertyValueFactory<>("material_id"));
        colMaterialName.setCellValueFactory(new PropertyValueFactory<>("material_name"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("remove"));

    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    @FXML
    void AddButtonOnAction(ActionEvent event) throws SQLException {
        boolean isValid = validateOrder();
        if(isValid) {
            Double inseam = Double.valueOf(txtInseam.getText());
            Double bust = Double.valueOf(txtBust.getText());
            Double hips = Double.valueOf(txtHips.getText());
            Double neck = Double.valueOf(txtNeck.getText());
            Double shoulder = Double.valueOf(txtShoulder.getText());
            Double waist = Double.valueOf(txtWaist.getText());

            String orderId = labelOrderId.getText();
            LocalDate date = LocalDate.parse(labelDate.getText());
            String customerId = comboCustomerId.getValue();
            double total = Double.parseDouble(Labeltotal.getText());
            PaymentModel modelP = new PaymentModel();
            String pay_id = modelP.generateNextId();
            System.out.println(pay_id);
            List<MaterialDressTm> materialList = new ArrayList<>();
            for (int i = 0; i < materialTable.getItems().size(); i++) {
                MaterialDressTm materialDressTm = obList.get(i);
                materialList.add(materialDressTm);
            }

            System.out.println("Place order form controller: " + materialList);

            PaymentDto payment = new PaymentDto(pay_id, date, total);
            OrderDto order = new OrderDto(pay_id, customerId, txtDate.getValue(), LocalDate.parse(labelDate.getText()),
                    labelOrderId.getText(), inseam, shoulder, neck, hips, waist, bust, txtDescription.getText(),
                    false, false);


            try {
                boolean isSaved = OrderModel.placeOrder(materialList, payment, order);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Order Success!").show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    @FXML
    void checkBoxOnAction(ActionEvent event) {
        ButtonType yes=new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no=new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> type=new Alert(Alert.AlertType.INFORMATION, "Is payment Success?", yes, no).showAndWait();
        if (type.orElse(no) == no) {
            checkBoxTable.setSelected(false);
        }
        else{
            checkBoxTable.setSelected(true);
        }


    }
    public  boolean validateOrder(){
        if(comboCustomerId.getValue() == null){
           new Alert(Alert.AlertType.ERROR, "Customer details empty").show();
           return false;
        }
        if(txtDate.getValue() == null){
            new Alert(Alert.AlertType.ERROR, "Date empty").show();
            return false;
        }
        boolean isValid=true;
        if((!txtWaist.getText().isEmpty())&&(!validateNumber("Waist", txtWaist.getText()))){

           return false;
        }

        if((!txtHips.getText().isEmpty())&&( !validateNumber("Hips", txtHips.getText()))){
            return false;
        }
        if((!txtNeck.getText().isEmpty())&&(!validateNumber("Neck", txtNeck.getText()))){
            return false;
        }
        if((!txtShoulder.getText().isEmpty())&&(!validateNumber("Shoulder", txtShoulder.getText()))){
            return false;
        }
        if((!txtBust.getText().isEmpty())&&(!validateNumber("Bust", txtBust.getText()))){
            return false;
        }
        if((!txtInseam.getText().isEmpty())&&(!validateNumber("Inseam", txtInseam.getText()))){
            return false;
        }
        System.out.println(Labeltotal.getText().isEmpty());
        if (tailorFees.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Tailor fees field is empty" ).show();
            return  false;
        }
        if(!validateNumber("Tailor fees", tailorFees.getText())){
            return false;
        }

        if(Labeltotal.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Total is empty...Click on Total button!!!" ).show();
            return false;
        }

        if (!checkBoxTable.isSelected()){
            new Alert(Alert.AlertType.ERROR,"payment is essential" ).show();
            return false;

        }
    return isValid;

    }

    private boolean validateNumber(String text,String value) {
        boolean matchAddress = Pattern.matches("^\\d+(\\.\\d{1,2})?$",value);
        if(!matchAddress){
            new Alert(Alert.AlertType.ERROR,"Invalid value for "+text ).show();
            return  false;
        }
        return true;
    }


}
