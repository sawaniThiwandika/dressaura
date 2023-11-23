package lk.ijse.dressaura.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.RentDto;
import lk.ijse.dressaura.model.OrderModel;
import lk.ijse.dressaura.model.RentModel;

import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import java.util.List;

import javafx.scene.control.Label;


public class DashboardController {
    @FXML
    private JFXButton addCustomerButton;

    @FXML
    private JFXButton addOrder;

    @FXML
    private JFXButton addRentBtn;

    @FXML
    private LineChart<?, ?> chart;

    @FXML
    private Label orderNum;

    @FXML
    private Label rentNumS;

    public void initialize() throws SQLException {
        updateLabels();

    }

    private void updateLabels() throws SQLException {
        RentModel model=new RentModel();
        OrderModel modelO=new OrderModel();
        List<RentDto> allRentals = model.getAllRentals();
       rentNumS.setText(String.valueOf(allRentals.size()));

    }
    @FXML
    void cusBtnOnAction(ActionEvent event) throws IOException {
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
    void orderBtnOnAction(ActionEvent event) throws IOException {
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
    void rentBtnOnAction(ActionEvent event) throws IOException {
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



}
