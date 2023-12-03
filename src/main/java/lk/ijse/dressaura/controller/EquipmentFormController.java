package lk.ijse.dressaura.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.EquipmentDto;
import lk.ijse.dressaura.dto.tm.EmployeeTm;
import lk.ijse.dressaura.dto.tm.EquipmentTm;
import lk.ijse.dressaura.model.EquipmentModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EquipmentFormController {
    @FXML
    private TableColumn<?, ?> colDelete;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colNum;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableView<EquipmentTm> tableEquipment;

    @FXML
    private TableColumn<?, ?> colUpdate;
    EquipmentModel model=new EquipmentModel();
    private ObservableList<EquipmentTm> obList = FXCollections.observableArrayList();
    public void addButtonOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/add_equipment_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Add equipment");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
    public void initialize() throws SQLException, IOException {
        setCellValueFactory();
        loadAllEquipment();

    }

    private void setCellValueFactory() {
        colNum.setCellValueFactory(new PropertyValueFactory<>("number"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("update"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }



    private void loadAllEquipment() throws SQLException {
        int i=1;

        List<EquipmentDto> equipmentList=model.getEquipmentTableValues();

        for (EquipmentDto eqDto:equipmentList) {
            Button btnU=new Button("Update");
            Button btnD=new Button("Delete");
            obList.add(new EquipmentTm(i,eqDto.getEquipmentId(),eqDto.getName(),eqDto.getPrice(),
                    eqDto.getDate(),btnU,btnD,eqDto.getDescription()));
            i++;
            deleteSupplierButtonOnAction(btnD,eqDto);
            updateButtonOnAction(btnU,eqDto);
            btnU.setCursor(Cursor.HAND);
            btnD.setCursor(Cursor.HAND);
        }
       tableEquipment.setItems(obList);

    }
    private void updateButtonOnAction(Button btnU, EquipmentDto equipmentDto) {
        btnU.setOnAction(event -> {
            try {
                openUpdateCustomerForm(equipmentDto);

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });




    }
    private void openUpdateCustomerForm(EquipmentDto equipmentDto) throws IOException, SQLException {

        URL resource = this.getClass().getResource("/view/add_equipment_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        AddEquipmentFormController controller = fxmlLoader.getController();
        controller.initialize(equipmentDto);
        Stage stage = new Stage();
        stage.setTitle("Update equipment");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(true);
        stage.show();

    }


    private void deleteSupplierButtonOnAction(Button btnD, EquipmentDto equipmentDto) {
        btnD.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int focusedIndex = tableEquipment.getSelectionModel().getSelectedIndex();
                EquipmentTm equipmentTm = tableEquipment.getSelectionModel().getSelectedItem();
                try {
                    boolean isDeleted = model.deleteEmployee(equipmentTm.getId());
                    if(isDeleted){
                        new Alert(Alert.AlertType.CONFIRMATION,"SUCCESSFULLLY deleted").show();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                obList.remove(focusedIndex);
                System.out.println(focusedIndex);
                tableEquipment.refresh();

            }
        });

    }

}
