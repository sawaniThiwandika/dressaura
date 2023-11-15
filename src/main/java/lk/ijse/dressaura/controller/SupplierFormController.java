package lk.ijse.dressaura.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.SupplierDto;
import lk.ijse.dressaura.dto.tm.SupplierTm;
import lk.ijse.dressaura.model.SupplierModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SupplierFormController {
    private ObservableList<SupplierTm> obList = FXCollections.observableArrayList();
    @FXML
    private Button addButton;

    @FXML
    private TableColumn<?, ?> colButtonDelete;

    @FXML
    private TableColumn<?, ?> colButtonUpdate;

    @FXML
    private TableColumn<?, ?> colNum;

    @FXML
    private TableColumn<?, ?> colSupContact;

    @FXML
    private TableColumn<?, ?> colSupEmail;

    @FXML
    private TableColumn<?, ?> colSupId;

    @FXML
    private TableColumn<?, ?> colSupName;

    @FXML
    private TableView<SupplierTm> supplierTable;
    SupplierModel supModel=new SupplierModel();
    public void initialize() throws SQLException, IOException {
        setCellValueFactory();
        loadAllSuppliers();

    }

    private void setCellValueFactory() {
        colNum.setCellValueFactory(new PropertyValueFactory<>("number"));
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colSupName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSupContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colSupEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colButtonUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colButtonDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }



    private void loadAllSuppliers() throws SQLException {
        int i=1;

        List<SupplierDto> supplierList=supModel.getSuplierTableValues();

        for (SupplierDto supDto:supplierList) {
            Button btnU=new Button("Update");
            Button btnD=new Button("Delete");
            obList.add(new SupplierTm(i,supDto.getSupId(),supDto.getName(),supDto.getContact(),supDto.getEmail(),btnU,btnD));
            i++;
            deleteSupplierButtonOnAction(btnD,supplierList);
            updateButtonOnAction(btnU,supDto);
            btnU.setCursor(Cursor.HAND);
            btnD.setCursor(Cursor.HAND);
        }
        supplierTable.setItems(obList);

    }

    private void updateButtonOnAction(Button btnU, SupplierDto supDto) {



    }

    private void deleteSupplierButtonOnAction(Button btnD,List<SupplierDto> supplierList) {


    }


    @FXML
    void addButtonOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/add_supplier_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Add supplier");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();


    }

}
