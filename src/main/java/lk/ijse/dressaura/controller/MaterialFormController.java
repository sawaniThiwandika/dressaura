package lk.ijse.dressaura.controller;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.MaterialDto;
import lk.ijse.dressaura.dto.tm.MaterialTm;
import lk.ijse.dressaura.model.MaterialModel;
import javafx.scene.control.Label;

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
    private TableColumn<?, ?> colAvalibility;

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


    }

    private void loadAllMaterials() throws SQLException {
        int i=1;

        List<MaterialDto> materialList=materialModel.getMaterialTableValues();

        for (MaterialDto dto:materialList) {
            String text=dto.getQtyOnHand()>0?"available":"not available";
            Button btnR=new Button("Restock");
            Button btnD=new Button("Delete");
            obList.add(new MaterialTm(i,dto.getMaterialId(), dto.getName(),dto.getUnitPrice(),dto.getQtyOnHand(),text
                    ,btnR ,btnD));
            i++;
            deleteMaterialButtonOnAction(btnD);
            restockButtonOnAction(btnR,dto);
            btnR.setCursor(javafx.scene.Cursor.HAND);
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

    private void deleteMaterialButtonOnAction(Button btnD) {

    }
}
