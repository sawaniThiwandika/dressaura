package lk.ijse.dressaura.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;



import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                    double col_qty= (double) colAmount.getCellData(i);

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
        var materialDress = new MaterialDressTm(id,name, unitPrice,qty, tot,btn);

        obList.add(materialDress);

        materialTable.setItems(obList);
        calculateTotal();
        txtAmount.clear();
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
        String orderId = labelOrderId.getText();
        LocalDate date = LocalDate.parse(labelDate.getText());
        String customerId = comboCustomerId.getValue();
        double total= Double.parseDouble(Labeltotal.getText());
        PaymentModel modelP=new PaymentModel();
        String pay_id= modelP.generateNextId();
        System.out.println(pay_id);
        List<MaterialDressTm> materialList = new ArrayList<>();
        for (int i = 0; i < materialTable.getItems().size(); i++) {
            MaterialDressTm materialDressTm = obList.get(i);
            materialList.add(materialDressTm);
        }

        System.out.println("Place order form controller: " + materialList);

        PaymentDto payment=new PaymentDto(pay_id,date,total);
        OrderDto order=new OrderDto(pay_id,customerId,txtDate.getValue(),LocalDate.parse(labelDate.getText()),labelOrderId.getText(),
                txtInseam.getText(),txtShoulder.getText(),txtNeck.getText(),txtHips.getText(),txtWaist.getText(),txtBust.getText(),txtDescription.getText());



       try {
           boolean isSaved = OrderModel.placeOrder(materialList, payment, order);
           if(isSaved){
               new Alert(Alert.AlertType.CONFIRMATION, "Order Success!").show();
           }
        } catch (SQLException e) {
           throw new RuntimeException(e);
       }

    }

}
