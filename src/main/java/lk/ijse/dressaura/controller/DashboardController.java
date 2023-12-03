package lk.ijse.dressaura.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.OrderDto;
import lk.ijse.dressaura.dto.RentDetailsDto;
import lk.ijse.dressaura.dto.RentDto;
import lk.ijse.dressaura.model.OrderModel;
import lk.ijse.dressaura.model.RentDetailsModel;
import lk.ijse.dressaura.model.RentModel;

import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;
import java.util.HashMap;
import java.util.Map;

public class DashboardController {
    @FXML
    private JFXButton addCustomerButton;

    @FXML
    private JFXButton addOrder;

    @FXML
    private JFXButton addRentBtn;


    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Label orderNum;

    @FXML
    private Label rentNumS;


    private Map<String, Integer> rentalData = new HashMap<>();
    public void initialize() throws SQLException {
        updateLabels();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
       RentDetailsModel modelR= new RentDetailsModel();
        List<RentDetailsDto> allRentals = modelR.getAllRentals();
        OrderModel modelO=new OrderModel();
        List<OrderDto> allIncompleteOrders = modelO.getAllIncompleteOrders();

        rentalData.put("Rent count", allRentals.size());
        rentalData.put("Order count", allIncompleteOrders.size());

        updateChart();
        applyCustomColors();

    }

    @FXML
    private void updateChart() {
        barChart.getData().clear();


        XYChart.Series<String, Number> series = new XYChart.Series<>();


        for (Map.Entry<String, Integer> entry : rentalData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }


        barChart.getData().add(series);
    }
    private void applyCustomColors() {
        for (XYChart.Series<String, Number> series : barChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                // Set custom color for each data point
                String style = "-fx-bar-fill:#5272F2 ;";
                data.getNode().setStyle(style);
            }
        }
    }

    private void updateLabels() throws SQLException {
        RentDetailsModel model=new RentDetailsModel();
        OrderModel modelO=new OrderModel();
        List<RentDetailsDto> allRentals = model.getAllRentals();
        List<Integer>currentList=new ArrayList<>();

        for(int i=0;i<allRentals.size();i++){
            if(allRentals.get(i).isReturn()==false){
                currentList.add(i);
            }
        }
        rentNumS.setText(String.valueOf(currentList.size()));

        List<OrderDto> allIncompleteOrders = modelO.getAllIncompleteOrders();
        orderNum.setText(String.valueOf(allIncompleteOrders.size()));


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
