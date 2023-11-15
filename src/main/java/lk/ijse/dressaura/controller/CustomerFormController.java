package lk.ijse.dressaura.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
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
import lk.ijse.dressaura.dto.CustomerDto;
import lk.ijse.dressaura.dto.tm.CustomerTm;
import lk.ijse.dressaura.model.CustomerModel;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustomerFormController {
    @FXML
    private Button add;
    @FXML
    private TableColumn<?, ?> columnAddress;

    @FXML
    private TableColumn<?, ?> columnContact;

    @FXML
    private TableColumn<?, ?> columnEmail;

    @FXML
    private TableColumn<?, ?> columnId;

    @FXML
    private TableColumn<?, ?> columnName;

    @FXML
    private TableColumn<?, ?> delete;

    @FXML
    private TableColumn<?, ?> update;
    @FXML
    private TableColumn<?, ?> number;

    @FXML
    private TableView<CustomerTm> tableCustomer;
    private ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
    private static CustomerModel cusModel=new CustomerModel();
    private CustomerDto setDto= new CustomerDto();



    @FXML
    void addButtonOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/add_customer_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Add customer");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
    public void initialize() throws SQLException, IOException {

        loadAllCustomers();
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        columnId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        update.setCellValueFactory(new PropertyValueFactory<>("update_button"));
        delete.setCellValueFactory(new PropertyValueFactory<>("delete_button"));

    }

    private void loadAllCustomers() throws SQLException, IOException {
        int i=1;

        List<CustomerDto> customerList=cusModel.getCustomerTableValues();

        for (CustomerDto cusDto:customerList) {
            Button btnU=new Button("Update");
            Button btnD=new Button("Delete");
            obList.add(new CustomerTm(i,cusDto.getCusId(), cusDto.getName(),cusDto.getAddress(),cusDto.getEmail(),cusDto.getContact(),btnU ,btnD));
            i++;
            deleteCustomerButtonOnAction(btnD);
            updateButtonOnAction(btnU,cusDto);
            btnU.setCursor(Cursor.HAND);
            btnD.setCursor(Cursor.HAND);
        }
        tableCustomer.setItems(obList);
    }

    private void updateButtonOnAction(Button btnU, CustomerDto customerDto) {
        btnU.setOnAction(event -> {
            try {
                setterDto(customerDto);

                openUpdateCustomerForm(customerDto);


            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setterDto(CustomerDto customerDto) {
        this.setDto=customerDto;
    }
    private CustomerDto getterDto() {
       return this.setDto;
    }

    private void openUpdateCustomerForm(CustomerDto customerDto) throws IOException, SQLException {

        URL resource = this.getClass().getResource("/view/update_customer_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        UpdateCustomerFormController controller = fxmlLoader.getController();
        controller.initialize(customerDto);
        Stage stage = new Stage();
        stage.setTitle("Update customer");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(true);
        stage.show();

    }


    private void deleteCustomerButtonOnAction(Button btnD) {
        btnD.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int focusedIndex = tableCustomer.getSelectionModel().getSelectedIndex();
                obList.remove(focusedIndex+1);
                System.out.println(focusedIndex);
                tableCustomer.refresh();

            }
        });

    }


    public void updateButtonOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/update_customer_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Update customer");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(true);
        stage.show();
    }
}
