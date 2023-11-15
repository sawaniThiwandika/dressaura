package lk.ijse.dressaura.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import lk.ijse.dressaura.dto.CustomerDto;
import lk.ijse.dressaura.dto.DressDto;
import lk.ijse.dressaura.dto.PaymentDto;
import lk.ijse.dressaura.dto.RentDto;
import lk.ijse.dressaura.model.CustomerModel;
import lk.ijse.dressaura.model.DressModel;
import lk.ijse.dressaura.model.PaymentModel;
import lk.ijse.dressaura.model.RentModel;

public class AddRentalFormController {
    @FXML
    private CheckBox checkDates;
    @FXML
    private Button add;

    @FXML
    private Button cancel;

    @FXML
    private CheckBox checkBoxPaid;

    @FXML
    private ComboBox<String> customerComboBox;

    @FXML
    private ComboBox<String> dressComboBox;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelRentId;

    @FXML
    private Label labelRentPrice;

    @FXML
    private Label lebalDressName;

    @FXML
    private Label lebalName;

    @FXML
    private AnchorPane newrental;


    @FXML
    private TableColumn<?, ?> colReservationAction;

    private RentModel rentModel= new RentModel();
    private CustomerModel cusModel= new CustomerModel();
    private DressModel dressModel= new DressModel();
    private PaymentModel payModel= new PaymentModel();
    @FXML
    private DatePicker txtRentDate;

    @FXML
    private DatePicker txtReturnDate;
    public void initialize() throws SQLException {


        generateNextRentId();
        setDate();
        loadCustomers();
        loadDresses();

    }

   public void setDetails(DressDto dto) {
        labelRentPrice.setText(String.valueOf(dto.getRentPrice()));
        lebalDressName.setText(dto.getName());
        dressComboBox.setValue(dto.getDressId());



    }

    private void loadDresses() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<DressDto> dresses = dressModel.getAllDress();

            for (DressDto dto : dresses) {
                obList.add(dto.getDressId());
            }
            dressComboBox.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCustomers() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<CustomerDto> customers =cusModel.getCustomerTableValues();

            for (CustomerDto dto : customers) {
                obList.add(dto.getCusId());
            }
            customerComboBox.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateNextRentId() throws SQLException {
        String nextId = rentModel.generateNextId();
        labelRentId.setText(nextId);
    }

    private void setDate() {
        labelDate.setText(String.valueOf(LocalDate.now()));
    }


    @FXML
    void cancelButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    @FXML
    void addButtonOnAction(ActionEvent event) throws SQLException, ParseException {
        try {
            String rentId = labelRentId.getText();
            String cusId = customerComboBox.getValue();
            String dressId = dressComboBox.getValue();
            boolean isPaid = checkBoxPaid.isSelected();
            boolean isAvelible = checkDates.isSelected();

            Double price = Double.parseDouble(labelRentPrice.getText());
            System.out.println(price);
            if (checkDateAvelible(dressId)) {
                if (checkBoxPaid.isSelected()){

                    System.out.println("sawani");
                //checkDateAvelible();


                String payId = generatePaymentId();
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate rent_date = txtRentDate.getValue();
                LocalDate return_date = txtReturnDate.getValue();
                LocalDate paid_date = LocalDate.now();

                int noOfDays = calculateDays(rent_date, rent_date);
                System.out.println(payId);
                RentDto rent = new RentDto(rentId, rent_date, return_date, noOfDays, dressId, cusId, payId, false, false);
                PaymentDto payment = new PaymentDto(payId, paid_date, price);

                try {
                    boolean isSaved = rentModel.saveRent(rent, payment);


                    if (isSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Successfully completed rent!").show();

                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Successfully completed rent!").show();
                }}
                else {
                    new Alert(Alert.AlertType.ERROR, "payment is essential").show();
                }

            }

            else {
                new Alert(Alert.AlertType.ERROR, "Not avelible period").show();
            }

        }
        catch (RuntimeException e){
           new Alert(Alert.AlertType.ERROR," have some Empty fields.. please filling them").show();

        }

    }

    private String generatePaymentId() throws SQLException {
        PaymentModel payModel=new PaymentModel();
        String pay_id = payModel.generateNextId();
        return pay_id;
    }

    private int calculateDays(LocalDate returnDate, LocalDate rentDate) {
        Period period = Period.between(rentDate, returnDate);
        int noOfDay = period.getDays();
        return noOfDay;
    }

    @FXML
    void comboCustomerOnAction(ActionEvent event) {
        String id = customerComboBox.getValue();
        try {
            CustomerDto customerDto = cusModel.searchCustomer(id);
            lebalName.setText(customerDto.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void comboDressOnAction(ActionEvent event) {
        String id = dressComboBox.getValue();
        try {
        DressDto dress = DressModel.searchDress(id);

            lebalDressName.setText(dress.getName());
            labelRentPrice.setText(String.valueOf(dress.getRentPrice()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void selectDressButtonOnAction(ActionEvent event) {


    }
    @FXML
    void checkDateOnAction(ActionEvent event) throws SQLException, ParseException {
        //checkDateAvelible();


    }

    private boolean checkDateAvelible(String dressId) throws ParseException, SQLException {
        LocalDate rent_date = txtRentDate.getValue();
        LocalDate return_date = txtReturnDate.getValue();


        List<RentDto> allRentals = rentModel.getAllRentals();
        boolean isAvealibility = rentModel.checkDateAvelibility(rent_date, return_date, allRentals,dressId);
        //if(isAvealibility==false) {
            //new Alert(Alert.AlertType.ERROR,"Not avelibile date").show();
           // checkDates.setSelected(false);
        //}
        boolean valid=(rent_date.isAfter(LocalDate.now()))&&(return_date.isAfter(LocalDate.now()))||(rent_date.isEqual(LocalDate.now()));
        System.out.println(valid);
        return isAvealibility&&valid;

    }

    @FXML
    void checkBoxPaidOnAction(ActionEvent event) {
        ButtonType yes=new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no=new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> type=new Alert(Alert.AlertType.INFORMATION, "Is payment Success?", yes, no).showAndWait();
        if (type.orElse(no) == no) {
            checkBoxPaid.setSelected(false);
        }
        else{
            checkBoxPaid.setSelected(true);
        }
    }
}
