package lk.ijse.dressaura.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import lk.ijse.dressaura.dto.RentDto;
import lk.ijse.dressaura.model.OrderModel;
import lk.ijse.dressaura.model.RentModel;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DashboardController {
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

}
