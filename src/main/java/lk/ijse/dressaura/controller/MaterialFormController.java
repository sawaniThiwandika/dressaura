package lk.ijse.dressaura.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lk.ijse.dressaura.dto.MaterialDto;
import lk.ijse.dressaura.dto.OrderDetailsDto;
import lk.ijse.dressaura.dto.tm.MaterialTm;
import lk.ijse.dressaura.model.MaterialModel;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lk.ijse.dressaura.model.OrderDetailModel;

public class MaterialFormController {
    @FXML
    private Button add;
    @FXML
    private Label labelCount;

    @FXML
    private Label labelLow;

    @FXML
    private Label labelOut;

    @FXML
    private TableColumn<MaterialTm, String> colAvalibility;

    @FXML
    private TableColumn<?, ?> colDelete;

    @FXML
    private TableColumn<?, ?> colMaterialId;

    @FXML
    private TableColumn<?, ?> colMaterialName;

    @FXML
    private TableColumn<?, ?> colNumber;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;

    @FXML
    private TableColumn<?, ?> colRestock;

    @FXML
    private TableColumn<?, ?> colUnitPrice;
    @FXML
    private TableView<MaterialTm> tableMaterial;


    private ObservableList<MaterialTm> obList = FXCollections.observableArrayList();
    private MaterialModel materialModel=new MaterialModel();

    @FXML
    void addButtonOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/add_material_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Add material");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
    public void initialize() throws SQLException {

        loadAllMaterials();
        setCellValueFactory();
        setValuesToLabels();


    }

    private void setValuesToLabels() throws SQLException {
        List<MaterialDto> materialTableValues = materialModel.getMaterialTableValues();
        List<Integer>lowList=new ArrayList<>();
        List<Integer> outList=new ArrayList<>();
        for(int i=0;i<materialTableValues.size();i++){
            double qtyOnHand = materialTableValues.get(i).getQtyOnHand();
            if(qtyOnHand==0){
                outList.add(i);
            }
            if(qtyOnHand<5&& qtyOnHand!=0){
                lowList.add(i);
            }
        }
        labelOut.setText(String.valueOf(outList.size()));
        labelLow.setText(String.valueOf(lowList.size()));
        labelCount.setText(String.valueOf(materialTableValues.size()));
    }

    private void setCellValueFactory() {
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colMaterialId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMaterialName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAvalibility.setCellValueFactory(new PropertyValueFactory<>("availability"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colRestock.setCellValueFactory(new PropertyValueFactory<>("restock"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        setColAvailabilityCellFactory();

    }
    private void setColAvailabilityCellFactory() {
        colAvalibility.setCellFactory(new Callback<TableColumn<MaterialTm, String>, TableCell<MaterialTm, String>>() {
            @Override
            public TableCell<MaterialTm, String> call(TableColumn<MaterialTm, String> param) {
                return new TableCell<MaterialTm, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty && item != null) {
                            Text text = new Text(item.toString());

                            if ("Available".equals(item.toString())) {
                                text.setFill(Color.GREEN);
                            } else {
                                text.setFill(Color.RED);
                            }

                            setGraphic(text);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }



    private void loadAllMaterials() throws SQLException {
        int i=1;

        List<MaterialDto> materialList=materialModel.getMaterialTableValues();

        for (MaterialDto dto:materialList) {

            String text=dto.getQtyOnHand()>0?"Available":"Not Available";
           /* if(text.equals("not available")){

            }
            if(text.equals("available")){

            }*/

            Button btnR=new Button("Restock");
            Button btnD=new Button("Delete");
            obList.add(new MaterialTm(i,dto.getMaterialId(), dto.getName(),dto.getUnitPrice(),dto.getQtyOnHand(),text
                    ,btnR ,btnD));
            i++;
            deleteMaterialButtonOnAction(btnD,dto);
            restockButtonOnAction(btnR,dto);
            btnR.setCursor(Cursor.HAND);
            btnD.setCursor(Cursor.HAND);
        }
        tableMaterial.setItems(obList);

    }

    private void restockButtonOnAction(Button btnR, MaterialDto dto) {
        btnR.setOnAction(event -> {
            try {

                openUpdateMaterialForm(dto);


            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void openUpdateMaterialForm(MaterialDto dto) throws IOException, SQLException {
        URL resource = this.getClass().getResource("/view/add_material_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        AddMaterialFormController controller = fxmlLoader.getController();
        controller.initialize(dto);
        Stage stage = new Stage();
        stage.setTitle("Update customer");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(true);
        stage.show();

    }

    private void deleteMaterialButtonOnAction(Button btnD,MaterialDto dto) throws SQLException {
        btnD.setOnAction(event -> {
        MaterialModel materialModel1=new MaterialModel();
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                boolean ongoing = false;
                try {
                    ongoing = checkOngoingOrder(dto);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (ongoing) {
                    new Alert(Alert.AlertType.ERROR, "Can not delete").show();
                } else {

                    boolean isDeleted = false;
                    try {
                        isDeleted = materialModel1.deleteMaterial(dto);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    if (isDeleted) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Successfully Deleted").show();
                    }

                }
            } });
    }

    private boolean checkOngoingOrder(MaterialDto dto) throws SQLException {
        OrderDetailModel orderDetailsModel=new OrderDetailModel();
        List<OrderDetailsDto> ongoingMaterials = OrderDetailModel.getAllDetails();
        for (int i=0;i<ongoingMaterials.size();i++){
            if(ongoingMaterials.get(i).getMaterialId().equals(dto.getMaterialId())){
                return true;

            }
        }
        return false;
    }
}
