package lk.ijse.dressaura.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.RentDto;
import lk.ijse.dressaura.model.OrderModel;
import lk.ijse.dressaura.model.RentDetailsModel;
import lk.ijse.dressaura.model.RentModel;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainFormController {

    @FXML
    private Button payment;
    @FXML
    private Button rental;
    @FXML
    private Button order;
    @FXML
    private Button customer;
    @FXML
    private Button dress;
    @FXML
    private Button employee;
    @FXML
    private Button material;
    @FXML
    private Button equipment;
    @FXML
    private Button dashboard;
    @FXML
    private AnchorPane mainForm;
    @FXML
    private Label count_orders;

    @FXML
    private Label labelDate;
    @FXML
    private Label count_rent;
    @FXML
    private Button logOutBtn;

    @FXML
    private Button supplier;
    @FXML
    private JFXButton addCustomerBtn;

    @FXML
    private JFXButton addOrderBtn;

    @FXML
    private JFXButton addRentBtn;
    @FXML
    void addCustomerBtnOnAction(ActionEvent event) throws IOException {
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

    @FXML
    void addOrderBtnOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/add_order_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Add order");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void addRentBtnOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/add_rental_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setTitle("Add rental");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();

    }
    public void initialize() throws SQLException, IOException {
       labelDate.setText(String.valueOf(LocalDate.now()));
        Parent root=FXMLLoader.load(this.getClass().getResource("/view/new_dashboard_form.fxml"));
        this.mainForm.getChildren().clear();
        this.mainForm.getChildren().add(root);
        checkLateReturn();
      //  updateLabels();

    }
    private void checkLateReturn() throws SQLException {
        RentDetailsModel rentDetailsModel=new RentDetailsModel();
        rentDetailsModel.checkLateReturn();

    }

    private void updateLabels() throws SQLException {
        RentModel model=new RentModel();
        OrderModel modelO=new OrderModel();
        List<RentDto> allRentals = model.getAllRentals();
        count_rent.setText(String.valueOf(allRentals.size()));

    }

    @FXML
    void logOutButtonOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) mainForm.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("dashboard");
        stage.centerOnScreen();
    }

    @FXML
        void paymentButtonOnAction(ActionEvent event) throws IOException {

        setForms("/view/payment_form.fxml");
        }
        @FXML
    void rentalButtonOnAction(ActionEvent event) throws IOException {
            setForms("/view/rental_form.fxml");
    }
    @FXML
    void orderButtonOnAction(ActionEvent event) throws IOException {
         setForms("/view/order_form.fxml");
    }
    @FXML
    void customerButtonOnAction(ActionEvent event) throws IOException {
        setForms("/view/customer_form.fxml");
    }
    @FXML
    void dressButtonOnAction(ActionEvent event) throws IOException {
        setForms("/view/dress_form.fxml");
    }
    @FXML
    void employeeButtonOnAction(ActionEvent event) throws IOException {
        setForms("/view/employee_form.fxml");
    }

    @FXML
    void materialButtonOnAction(ActionEvent event) throws IOException {
        setForms("/view/material_form.fxml");
    }
    @FXML
    void equipmentButtonOnAction(ActionEvent event) throws IOException {
        setForms("/view/equipment_form.fxml");

    }
    @FXML
    void dashboardButtonOnAction(ActionEvent event) throws IOException {
        setForms("/view/new_dashboard_form.fxml");

    }

    public void supplierButtonOnAction(ActionEvent actionEvent) throws IOException {
        setForms("/view/supplier_form.fxml");
    }

    public void setForms(String forms) throws IOException {
        String[] form = {"/view/new_dashboard_form.fxml",
                "/view/payment_form.fxml",
                "/view/rental_form.fxml",
                "/view/order_form.fxml",
                "/view/customer_form.fxml",
                "/view/dress_form.fxml",
                "/view/employee_form.fxml",
                "/view/material_form.fxml",
                "/view/supplier_form.fxml",
                "/view/equipment_form.fxml"

        };

        Button[] btn = {
                dashboard,payment,rental,order,customer,dress,employee,material,supplier,equipment};
        AnchorPane load = FXMLLoader.load(getClass().getResource(forms));
        mainForm.getChildren().clear();
        mainForm.getChildren().add(load);

        for (int i = 0; i < form.length; i++) {
            btn[i].setStyle("-fx-background-color:  #232C37");
            if (forms.equals(form[i])) {
                btn[i].setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000");
            }
        }

    }


}

