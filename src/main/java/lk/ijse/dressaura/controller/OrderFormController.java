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
import lk.ijse.dressaura.dto.OrderDetailsDto;
import lk.ijse.dressaura.dto.OrderDto;
import lk.ijse.dressaura.dto.tm.OrderTm;
import lk.ijse.dressaura.model.CustomerModel;
import lk.ijse.dressaura.model.OrderDetailModel;
import lk.ijse.dressaura.model.OrderModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderFormController {
    @FXML
    private TableColumn<?, ?> colCusId;

    @FXML
    private TableColumn<?, ?> colCusName;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDelete;

    @FXML
    private TableColumn<?, ?> colNum;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colReturnDate;

    @FXML
    private TableColumn<?, ?> colViewDetail;

    @FXML
    private TableView<OrderTm> tableOrder;
    @FXML
    private TableColumn<?, ?> colFinished;
    @FXML
    private TableColumn<?, ?> colHandOver;
    @FXML
    private Label noOfOrders;
    @FXML
    private Button add;
    OrderModel orderModel=new OrderModel();
   CustomerModel cusModel=new CustomerModel();
   OrderDetailModel orderDetailModel=new OrderDetailModel();
    private ObservableList<OrderTm> obList = FXCollections.observableArrayList();
    @FXML
    void AddButtonOnAction(ActionEvent event) throws IOException {
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
    public void initialize() throws SQLException {

        loadAllIncompletedOrders();
        setCellValueFactory();
        noOfOrders.setText(String.valueOf(orderModel.getAllIncompleteOrders().size()));

    }

    private void setCellValueFactory() {
        colNum.setCellValueFactory(new PropertyValueFactory<>("number"));
        colCusId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        colFinished.setCellValueFactory(new PropertyValueFactory<>("finish"));
        colHandOver.setCellValueFactory(new PropertyValueFactory<>("handOver"));
        colViewDetail.setCellValueFactory(new PropertyValueFactory<>("view"));
    }

    private void loadAllIncompletedOrders() throws SQLException {
        int i=1;
        List<OrderDto> orders=orderModel.getAllIncompleteOrders();



        for (OrderDto dto:orders) {
            ArrayList<OrderDetailsDto> orderDetails = orderDetailModel.getOrderDetails(dto.getOrderId());
            Button btnV=new Button("View");
            Button btnF=new Button("Finished");
            Button btnH=new Button("HandOver");
            Button btnD=new Button("Delete");
            obList.add(new OrderTm(i,dto.getOrderId(),dto.getCusId(),cusModel.searchCustomer(dto.getCusId()).getName(),
                    dto.getDate(),dto.getReturnDate(),btnV,btnF ,btnH,btnD));
            i++;
            deleteButtonOnAction(btnD,dto);
            viewButtonOnAction(btnV,dto,orderDetails);
            finishButtonOnAction(btnF,dto);
            handOverButtonOnAction(btnH,dto);
            btnV.setCursor(Cursor.HAND);
            btnD.setCursor(Cursor.HAND);
            btnF.setCursor(Cursor.HAND);
            btnH.setCursor(Cursor.HAND);
        }
        tableOrder.setItems(obList);

    }

    private void handOverButtonOnAction(Button btnH, OrderDto dto) {
        btnH.setOnAction(event -> {
            dto.setIsHandOver(true);


            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure that order has handed over?",
                    yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                try {
                    boolean isUpdated = orderModel.updateOrder(dto);
                    if(isUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION,"Successfully updated").show();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
        }


        });



    }

    private void finishButtonOnAction(Button btnF, OrderDto dto) {
        btnF.setOnAction(event -> {
            dto.setIsCompleted(true);

            try {
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure that order has finished?", yes, no).showAndWait();

                if (type.orElse(no) == yes) {
                orderModel.sendNotification(dto);
                    boolean isUpdated = orderModel.updateOrder(dto);
                   if(isUpdated ){new Alert(Alert.AlertType.CONFIRMATION,"Successfully send email notification to the customer").show();}}
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        });
    }

    private void viewButtonOnAction(Button btnV, OrderDto dto, ArrayList<OrderDetailsDto> orderDetails) {
        btnV.setOnAction(event -> {

            try {
                openViewMeasurementForm(dto,orderDetails);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        });

    }

    private void openViewMeasurementForm(OrderDto dto, ArrayList<OrderDetailsDto> orderDetails) throws IOException, SQLException {
        URL resource = this.getClass().getResource("/view/viewMeasurement_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        ViewMeasurementFormController controller = fxmlLoader.getController();
        controller.initialize(dto,orderDetails);
        Stage stage = new Stage();
        stage.setTitle("view measurements");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(true);
        stage.show();


    }

    private void deleteButtonOnAction(Button btnD, OrderDto dto) throws SQLException {


        btnD.setOnAction(event -> {

            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure remove this order?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {

            try {
                boolean isDeleted = orderModel.deleteOrder(dto);
              if  (isDeleted){
                  new Alert(Alert.AlertType.CONFIRMATION,"Successfully delete the order..").show();
                  tableOrder.refresh();
              }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }});



    }


}
