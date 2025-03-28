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
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import lk.ijse.dressaura.dto.EmployeeDto;
import lk.ijse.dressaura.dto.tm.CustomerTm;
import lk.ijse.dressaura.dto.tm.EmployeeTm;
import lk.ijse.dressaura.model.EmployeeModel;



public class EmployeeFormController {
    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colDelete;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colEmpId;

    @FXML
    private TableColumn<?, ?> colJobRole;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colNum;

    @FXML
    private TableColumn<?, ?> colUpdate;

    @FXML
    private TableView<EmployeeTm> employeeTable;
    @FXML
    private Label noOfEmployees;
    EmployeeModel empModel= new EmployeeModel();
    private ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();
    @FXML
    private Button cancel;
    public void addButtonOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/add_employee_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Add employee");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
    public void initialize() throws SQLException, IOException {

        loadAllEmployee();
        setCellValueFactory();
        noOfEmployees.setText(String.valueOf(empModel.getEmployeeTableValues().size()));
    }

    private void loadAllEmployee() throws SQLException {
        int i=1;

        List<EmployeeDto> empList=empModel.getEmployeeTableValues();

        for (EmployeeDto empDto:empList) {
            Button btnU=new Button("Update");
            Button btnD=new Button("Delete");
            obList.add(new EmployeeTm(i,empDto.getEmpId(), empDto.getName(),empDto.getAddress(),empDto.getEmail(),
                    empDto.getContact(),btnU ,btnD,empDto.getJobRole()));
            i++;
            deleteEmployeeButtonOnAction(btnD);
            updateButtonOnAction(btnU,empDto);
            btnU.setCursor(Cursor.HAND);
            btnD.setCursor(Cursor.HAND);
        }
        employeeTable.setItems(obList);
    }

    private void updateButtonOnAction(Button btnU, EmployeeDto empDto) {
        btnU.setOnAction(event -> {
            try {

                openUpdateMaterialForm(empDto);


            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }


    private void openUpdateMaterialForm(EmployeeDto dto) throws IOException, SQLException {
        URL resource = this.getClass().getResource("/view/add_employee_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        AddEmployeeFormController controller = fxmlLoader.getController();
        controller.initialize(dto);
        Stage stage = new Stage();
        stage.setTitle("Update customer");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(true);
        stage.show();

    }



    private void deleteEmployeeButtonOnAction(Button btnD) {
        btnD.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int focusedIndex = employeeTable.getSelectionModel().getSelectedIndex();
                EmployeeTm employeeTm = employeeTable.getSelectionModel().getSelectedItem();
                try {
                    boolean isDeleted = empModel.deleteEmployee(employeeTm.getEmpId());
                    if(isDeleted){
                        new Alert(Alert.AlertType.CONFIRMATION,"SUCCESSFULLLY deleted").show();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                obList.remove(focusedIndex);
                System.out.println(focusedIndex);
                employeeTable.refresh();

            }
        });

    }

    private void setCellValueFactory() {
        colNum.setCellValueFactory(new PropertyValueFactory<>("number"));
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colJobRole.setCellValueFactory(new PropertyValueFactory<>("jobRole"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("update"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));

    }

}
