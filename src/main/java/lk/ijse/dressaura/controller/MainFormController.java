package lk.ijse.dressaura.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;


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
        void paymentButtonOnAction(ActionEvent event) throws IOException {

           Parent form = FXMLLoader.load(getClass().getResource("/view/payment_form.fxml"));

            this.mainForm.getChildren().clear();
            this.mainForm.getChildren().add(form);
        }
        @FXML
    void rentalButtonOnAction(ActionEvent event) throws IOException {
            Parent form = FXMLLoader.load(getClass().getResource("/view/rental_form.fxml"));

            this.mainForm.getChildren().clear();
            this.mainForm.getChildren().add(form);
    }
    @FXML
    void orderButtonOnAction(ActionEvent event) throws IOException {
        Parent form = FXMLLoader.load(getClass().getResource("/view/order_form.fxml"));

        this.mainForm.getChildren().clear();
        this.mainForm.getChildren().add(form);
    }
    @FXML
    void customerButtonOnAction(ActionEvent event) throws IOException {
        Parent form = FXMLLoader.load(getClass().getResource("/view/customer_form.fxml"));

        this.mainForm.getChildren().clear();
        this.mainForm.getChildren().add(form);
    }
    @FXML
    void dressButtonOnAction(ActionEvent event) throws IOException {
        Parent form = FXMLLoader.load(getClass().getResource("/view/dress_form.fxml"));
        this.mainForm.getChildren().clear();
        this.mainForm.getChildren().add(form);
    }
    @FXML
    void employeeButtonOnAction(ActionEvent event) throws IOException {
        Parent form = FXMLLoader.load(getClass().getResource("/view/employee_form.fxml"));
        this.mainForm.getChildren().clear();
        this.mainForm.getChildren().add(form);
    }

    @FXML
    void materialButtonOnAction(ActionEvent event) throws IOException {
        Parent form = FXMLLoader.load(getClass().getResource("/view/material_form.fxml"));
        this.mainForm.getChildren().clear();
        this.mainForm.getChildren().add(form);
    }
    @FXML
    void equipmentButtonOnAction(ActionEvent event) throws IOException {
        Parent form = FXMLLoader.load(getClass().getResource("/view/equipment_form.fxml"));
       this.mainForm.getChildren().clear();
        this.mainForm.getChildren().add(form);
    }
    @FXML
    void dashboardButtonOnAction(ActionEvent event) throws IOException {
        Parent form = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        this.mainForm.getChildren().clear();
        this.mainForm.getChildren().add(form);
    }

}

