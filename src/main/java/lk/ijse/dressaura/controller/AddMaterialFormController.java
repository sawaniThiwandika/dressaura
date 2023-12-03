package lk.ijse.dressaura.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.*;
import lk.ijse.dressaura.dto.tm.MaterialDressTm;
import lk.ijse.dressaura.dto.tm.MaterialSupplierTm;
import lk.ijse.dressaura.model.MaterialModel;
import lk.ijse.dressaura.model.PaymentModel;
import lk.ijse.dressaura.model.SupplierModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @FXML
    private TableColumn<?, ?> colAmout;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colSellingPrice;

    @FXML
    private TableColumn<?, ?> colTotal;
    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private ListView<String> listMaterial;

    @FXML
    private ListView<String> listSupplier;

    @FXML
    private TextField searchMaterial;

    @FXML
    private TextField searchSupplier;

    @FXML
    private TableView<MaterialSupplierTm> tableCart;
    private MaterialModel materialModel=new MaterialModel();
    private SupplierModel supplierModel=new SupplierModel();
    ObservableList<String> obListSupplier = FXCollections.observableArrayList();
    ObservableList<String> obListMaterial = FXCollections.observableArrayList();
    private ObservableList<MaterialSupplierTm> obList = FXCollections.observableArrayList();
    private int currentSelectedMaterialIndex = -1;
    private int currentSelectedSupplierIndex = -1;
    public void initialize() throws SQLException {

        List<SupplierDto> supplierDtos =supplierModel.getSuplierTableValues();
        List<MaterialDto> materialDtos =materialModel.getMaterialTableValues();
        setCellValueFactory();
        genarateMaterial();
        setDate();
       // setcomboBoxValues();
        setSupplierList(supplierDtos);
        setMaterialList(materialDtos);

        //setValues(dto);
    }
    public void initialize(MaterialDto dto) throws SQLException {
        List<SupplierDto> supplierDtos =supplierModel.getSuplierTableValues();
        List<MaterialDto> materialDtos =materialModel.getMaterialTableValues();
        genarateMaterial();
        setDate();
        setCellValueFactory();
        //setcomboBoxValues();
        //setSupplierList(supplierDtos);
        setMaterialList(materialDtos);
        setValues(dto);
    }
    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("material_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("material_name"));
        colAmout.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("remove"));

    }

    private void setMaterialList(List<MaterialDto> materialDtos) {
        for (MaterialDto dto : materialDtos) {
            obListMaterial.add(dto.getMaterialId());
        }
        listMaterial.setItems(obListMaterial);
        searchMaterial.textProperty().addListener((observable, oldValue, newValue) -> {
            listMaterial.setItems(filterMaterialData(obListMaterial, newValue));
        });
    }

    private ObservableList<String> filterMaterialData(ObservableList<String> originalData, String filter) {
        if (filter == null || filter.isEmpty()) {
            return originalData;
        }

        ObservableList<String> filteredData = FXCollections.observableArrayList();
        for (String item : originalData) {
            if (item.toLowerCase().contains(filter.toLowerCase())) {
                filteredData.add(item);
            }
        }
        return filteredData;
    }

    private void setSupplierList(List<SupplierDto> supplierDtos) {

            for (SupplierDto dto : supplierDtos) {
                obListSupplier.add(dto.getContact());
            }
            listSupplier.setItems(obListSupplier);
            searchSupplier.textProperty().addListener((observable, oldValue, newValue) -> {
            listSupplier.setItems(filterSupplierData(obListSupplier, newValue));
        });
    }

    private ObservableList<String> filterSupplierData(ObservableList<String> originalData, String filter) {
        if (filter == null || filter.isEmpty()) {
            return originalData;
        }

        ObservableList<String> filteredData = FXCollections.observableArrayList();
        for (String item : originalData) {
            if (item.toLowerCase().contains(filter.toLowerCase())) {
                filteredData.add(item);
            }
        }
        return filteredData;
    }


    public  void setValues(MaterialDto dto) throws SQLException {
        labelMaterialId.setText(dto.getMaterialId());
        txtMaterial.setText(dto.getName());
        txtSellPrice.setText(String.valueOf(dto.getUnitPrice()));
        searchMaterial.setText(dto.getMaterialId());
        if(dto.getMaterialId().isEmpty()){
            genarateMaterial();

         // setcomboBoxValues();

        }


    }

   /* private void setcomboBoxValues() {
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

    }*/
    @FXML
    void comboSupplierOnAction(ActionEvent event) throws SQLException {
        String supId = comboSupplierId.getValue();
       // labelSupplierName.setText(supplierModel.searchSupplier(supId).getName());

    }


    private String genarateMaterial() throws SQLException {
        MaterialModel materialModel=new MaterialModel();
        String m_id =materialModel.generateNextId();
        searchMaterial.setText(m_id);
        return m_id;


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

            //LocalDate date = LocalDate.parse(labelDate.getText());
            LocalDate date=LocalDate.now();
            //String m_id = searchMaterial.getText();
            //String value = (String) comboSupplierId.getValue();
            String supContact = searchSupplier.getText();
            //Double amount = Double.valueOf(txtAmount.getText());
           // String m_name = txtMaterial.getText();
           // Double price = Double.valueOf(txtPrice.getText());
           // Double sellPrice  = Double.valueOf(txtSellPrice.getText());
            Double cost= Double.valueOf(labelCost.getText());

            List<MaterialSupplierTm> materialList = new ArrayList<>();
            for (int i = 0; i < tableCart.getItems().size(); i++) {
                 MaterialSupplierTm material = obList.get(i);
                 materialList.add(material);
            }

           // MaterialDto dtoM= new MaterialDto(m_id,amount,sellPrice,m_name);
            PaymentModel modelp=new PaymentModel();
            String pay_id = modelp.generateNextId();
            PaymentDto dtoP=new PaymentDto(pay_id,date,cost);
            //SupplierDetailsDto dtoSD=new SupplierDetailsDto(supContact,materialList);

            boolean isSaved = materialModel.saveMaterial(materialList, dtoP,supContact);
            if(isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"successfully saved").show();
            }



    }

    private boolean validateMaterial() {

        if(searchSupplier.getText().isEmpty()){
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
       // if(!checkbtn.isSelected()){
            //new Alert(Alert.AlertType.ERROR,"Empty fields... please check in").show();
           // return  false;
      //  }

        return true;

    }

    @FXML
    void checkboxOnAction(ActionEvent event) throws SQLException {
        boolean isValid=validateMaterial();
        if (isValid) {
            String id = searchMaterial.getText();
            String name = txtMaterial.getText();
            double qty = Double.parseDouble(txtAmount.getText());
            double price = Double.parseDouble(txtPrice.getText());
            double sellingPrice = Double.parseDouble(txtSellPrice.getText());
            double tot = price * qty;
            Button btn = new Button("Remove");

            setRemoveBtnAction(btn);
            btn.setCursor(Cursor.HAND);


            if (!obList.isEmpty()) {
                for (int i = 0; i < tableCart.getItems().size(); i++) {
                    if (colId.getCellData(i).equals(id)) {
                        double col_qty = (double) colAmout.getCellData(i);
                        qty += col_qty;
                        tot = price * qty;

                        obList.get(i).setAmount(qty);
                        obList.get(i).setTotal(tot);

                        calculateTotal();
                        tableCart.refresh();
                        return;
                    }
                }
            }
            MaterialSupplierTm material = new MaterialSupplierTm(id, name, sellingPrice, qty, tot, btn);

            obList.add(material);
            tableCart.setItems(obList);
            calculateTotal();
            txtAmount.clear();
            String m_id = genarateMaterial();
            String [] split= m_id.split("M0");
            int ID=Integer.parseInt(split[1]);

            ID++;
            searchMaterial.setText("M0"+ID);

        }
    }

    private void calculateTotal() {
        double total = 0;
        for (int i = 0; i < tableCart.getItems().size(); i++) {
            total += (double) colTotal.getCellData(i);
        }
        labelCost.setText(String.valueOf(total));
    }

    private void setRemoveBtnAction(Button btn) {
        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int focusedIndex = tableCart.getSelectionModel().getSelectedIndex();

                obList.remove(focusedIndex+1);

                tableCart.refresh();


                calculateTotal();
            }
        });

    }


    public void handleKeyPress(KeyEvent event) {
        if (event.getCode().isArrowKey()) {
            if (event.getCode().isNavigationKey()) {
                event.consume();
            }
        }
    }

    @FXML
    public void materialListOnMouseClicked(MouseEvent mouseEvent) throws SQLException {
        String selectedItem =listMaterial.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println("Item clicked: " + selectedItem);
            searchMaterial.setText(selectedItem);
            txtMaterial.setText(materialModel.searchMaterial(selectedItem).getName());
        }
    }

    public void supplierListOnMouseClicked(MouseEvent mouseEvent) throws SQLException {
        String selectedItem = listSupplier.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println("Item clicked: " + selectedItem);
            System.out.println(supplierModel.searchSupplier(selectedItem).getName());
            searchSupplier.setText(selectedItem);

        }
    }
    @FXML
    void materialOnKeyReleased(KeyEvent event) throws SQLException {
        if (event.getCode().isArrowKey()) {
            // Handle arrow key navigation and selection
            if (event.getCode().isNavigationKey()) {
                if (event.getCode() == KeyCode.DOWN) {
                    currentSelectedMaterialIndex = Math.min(currentSelectedMaterialIndex + 1, obListMaterial.size() - 1);
                } else if (event.getCode() == KeyCode.UP) {
                    currentSelectedMaterialIndex = Math.max(currentSelectedMaterialIndex - 1, 0);
                }

                listMaterial.getSelectionModel().select(currentSelectedMaterialIndex);
                txtMaterial.setText(materialModel.searchMaterial(listMaterial.getSelectionModel().getSelectedItem()).getName());
                txtSellPrice.setText(String.valueOf(materialModel.searchMaterial(listMaterial.getSelectionModel().getSelectedItem()).getUnitPrice()));
                listMaterial.scrollTo(currentSelectedMaterialIndex);
            }
            event.consume(); // Consume the event to prevent it from being processed twice
        } else if (event.getCode() == KeyCode.ENTER) {
            // Handle Enter key press
            String selectedItem =listMaterial.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                System.out.println("Enter key pressed on: " + selectedItem);
                // Add your custom action here
                setMaterialSearchFieldText(selectedItem);
                hideMaterialListView();
            }
        } else {
            // Handle other keys for filtering
            filterMaterialList();
            showMaterialListView();
        }
    }

    private void showMaterialListView() {
        listMaterial.setVisible(true);
    }

    private void filterMaterialList() {
        String filter = searchMaterial.getText().toLowerCase();

        if (filter.isEmpty()) {
            listMaterial.setItems(obListMaterial);
            return;
        }

        ObservableList<String> filteredData = FXCollections.observableArrayList();
        for (String item :obListMaterial) {
            if (item.toLowerCase().contains(filter)) {
                filteredData.add(item);
            }
        }

        listMaterial.setItems(filteredData);
        currentSelectedMaterialIndex = -1;
    }

    private void hideMaterialListView() {
        listMaterial.setVisible(false);
    }

    private void setMaterialSearchFieldText(String selectedItem) {
        searchMaterial.setText(selectedItem);
    }

    @FXML
    void supplierOnKeyReleased(KeyEvent event) throws SQLException {
        if (event.getCode().isArrowKey()) {
            // Handle arrow key navigation and selection
            if (event.getCode().isNavigationKey()) {
                if (event.getCode() == KeyCode.DOWN) {
                    currentSelectedSupplierIndex = Math.min(currentSelectedSupplierIndex + 1, obListSupplier.size() - 1);
                } else if (event.getCode() == KeyCode.UP) {
                    currentSelectedSupplierIndex = Math.max(currentSelectedSupplierIndex - 1, 0);
                }

                listSupplier.getSelectionModel().select(currentSelectedSupplierIndex);
                labelSupplierName.setText(supplierModel.searchSupplier(listSupplier.getSelectionModel().getSelectedItem()).getName());
                listSupplier.scrollTo(currentSelectedSupplierIndex);
            }
            event.consume(); // Consume the event to prevent it from being processed twice
        } else if (event.getCode() == KeyCode.ENTER) {
            String selectedItem =listSupplier.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                System.out.println("Enter key pressed on: " + selectedItem);
                setSupplierSearchFieldText(selectedItem);
                System.out.println(supplierModel.searchSupplier(selectedItem).getSupId());

                hideSupplierListView();
            }
        } else {
            filterSupplierList();
            showSupplierListView();
        }
    }

    private void filterSupplierList() {
        String filter =searchSupplier.getText().toLowerCase();

        if (filter.isEmpty()) {
           listSupplier.setItems(obListSupplier);
            return;
        }

        ObservableList<String> filteredData = FXCollections.observableArrayList();
        for (String item : obListSupplier) {
            if (item.toLowerCase().contains(filter)) {
                filteredData.add(item);
            }
        }

        listSupplier.setItems(filteredData);
        currentSelectedSupplierIndex = -1;

    }

    private void showSupplierListView() {
        listSupplier.setVisible(true);
    }


    private void setSupplierSearchFieldText(String selectedItem) {

        searchSupplier.setText(selectedItem);
    }

    private void hideSupplierListView() {
        listSupplier.setVisible(false);
    }

}
