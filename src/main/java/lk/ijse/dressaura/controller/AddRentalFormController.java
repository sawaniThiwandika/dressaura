package lk.ijse.dressaura.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.*;
import lk.ijse.dressaura.dto.tm.RentDressCartTm;
import lk.ijse.dressaura.model.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class AddRentalFormController {
    @FXML
    private Label labelTotal;

    @FXML
    private Label labelPayment;

    @FXML
    private ComboBox<String> cmbPaymentType;
    @FXML
    private JFXButton addToCartBtn;
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
    private RentDetailsModel rentDetailsModel= new RentDetailsModel();
    private AddRentModel addrentModel= new AddRentModel();
    private CustomerModel cusModel= new CustomerModel();
    private DressModel dressModel= new DressModel();
    private PaymentModel payModel= new PaymentModel();
    private RentDto rents;
    @FXML
    private DatePicker txtRentDate;

    @FXML
    private DatePicker txtReturnDate;
    @FXML
    private TableView<RentDressCartTm> cartTable;
    @FXML
    private TableColumn<?, ?> colDressId;

    @FXML
    private TableColumn<?, ?> colDressName;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colReservationDate;

    @FXML
    private TableColumn<?, ?> colReturnDate;
    @FXML
    private TableColumn<?, ?> colRemove;
    @FXML
    private TableColumn<?, ?> colUpdate;
    @FXML
    private Button viewRental;
    @FXML
    private JFXButton invoice;
    ObservableList<RentDressCartTm> rentList=FXCollections.observableArrayList();
    ArrayList<String>rentIds=new ArrayList<>();

    public void initialize() throws SQLException {

        generateNextRentId();
        setDate();
        loadCustomers();
        loadDresses();
        loadPaymentTypes();
        setCellValueFactory();

    }
    @FXML
    void viewRentalButtonOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/viewRentList_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        ViewRentalFormControlller controller = fxmlLoader.getController();
        controller.setValues(dressComboBox.getValue());
        Stage stage = new Stage();
        stage.setTitle("view rents");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();


    }

    private void setCellValueFactory() {
        colDressId.setCellValueFactory(new PropertyValueFactory<>("dressId"));
        colDressName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colReservationDate.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("remove"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("update"));
    }

    private void loadPaymentTypes() {
        ObservableList<String>  types = FXCollections.observableArrayList("Advanced","Full");
        cmbPaymentType.setItems(types);
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

    private String generateNextRentId() throws SQLException {
        String nextId = rentModel.generateNextId();
        labelRentId.setText(nextId);
        return nextId;
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
    void addToCartButtonOnAction(ActionEvent event) throws SQLException {

        if(checkDates.isSelected()) {
            String rentId=labelRentId.getText();
            //rentIds.add(rentId);
            String dressId=dressComboBox.getValue();
            String dressName = lebalDressName.getText();
            Double price = Double.valueOf(labelRentPrice.getText());
            LocalDate reservationDate = txtRentDate.getValue();
            LocalDate returnDate = txtReturnDate.getValue();
            Button remove = new Button("remove");
            Button update = new Button("update");

            RentDressCartTm cartTm = new RentDressCartTm(rentId,dressId, dressName, reservationDate, returnDate, price,
                    remove,update);
            setRemoveButtonAction(remove);
            setUpdateButtonAction(update);
            rentList.add(cartTm);
            cartTable.setItems(rentList);
            calculateTotal();
            checkDates.setSelected(false);
            //clearFields();
            //rentId=rentModel.genatateNewRentIdForMoreRents(rentId);
            System.out.println(rentId);
          // labelRentId.setText(rentId);
        }

    }

    private void setUpdateButtonAction(Button update) {
       update.setOnAction((e) -> {
                int focusedIndex = cartTable.getSelectionModel().getSelectedIndex()+1;
                txtReturnDate.setValue((LocalDate) colReturnDate.getCellData(focusedIndex));
                txtRentDate.setValue((LocalDate) colReservationDate.getCellData(focusedIndex));
                lebalDressName.setText((String) colDressName.getCellData(focusedIndex));
                dressComboBox.setValue((String) colDressId.getCellData(focusedIndex));
                rentList.remove(focusedIndex);
                cartTable.refresh();
                cartTable.refresh();
                calculateTotal();

        });

    }

    private void clearFields() {
        //labelRentId.setText("");
        dressComboBox.setValue(null);
        labelRentPrice.setText("");
        lebalDressName.setText("");
        txtReturnDate.setValue(null);
        txtRentDate.setValue(null);
    }

    private void calculateTotal() {
        double total = 0;
        for (int i = 0; i < cartTable.getItems().size(); i++) {
            total += (double) colPrice.getCellData(i);
        }
        labelTotal.setText(String.valueOf(total));
    }

    private void setRemoveButtonAction(Button remove) {
       remove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int focusedIndex = cartTable.getSelectionModel().getSelectedIndex();

                rentList.remove(focusedIndex+1);
                cartTable.refresh();
                calculateTotal();
            }
        });

    }

    @FXML
    void cmbPaymentTypeOnAction(ActionEvent event) {
        if(cmbPaymentType.getValue().equals("Advanced")){
            labelPayment.setText(String.valueOf(Double.valueOf(labelTotal.getText())/2));
        }
        else{
            labelPayment.setText(labelTotal.getText());
        }


    }
    @FXML
    void addButtonOnAction(ActionEvent event) throws SQLException, ParseException {
        try {

            String type=cmbPaymentType.getValue();
            String cusId = customerComboBox.getValue();
            boolean isPaid = checkBoxPaid.isSelected();
            boolean isAvelible = checkDates.isSelected();
            boolean paidComplete=false;
            if(type.equals("Advanced")){
                paidComplete=false;
            }
            else {
                paidComplete=true;
            }


            Double price = Double.parseDouble(labelPayment.getText());
            System.out.println(price);
            if (checkBoxPaid.isSelected()){
                String payId = generatePaymentId();
                LocalDate paid_date = LocalDate.now();


                System.out.println(payId);
                //RentDto rent = new RentDto(rentId, rent_date, return_date, noOfDays, dressId, cusId, payId, false, false);
                PaymentDto payment = new PaymentDto(payId, paid_date, price);
                   // List <RentDto> rents=new ArrayList<>();
                    rents=new RentDto(labelRentId.getText(),customerComboBox.getValue(),payId,LocalDate.now(),paidComplete);
                    List <RentDetailsDto> rentDetails=new ArrayList<>();
                    for (int i = 0; i < cartTable.getItems().size(); i++) {
                        rentDetails.add(new RentDetailsDto(rentList.get(i).getDressId(),labelRentId.getText(),rentList.get(i).getRentDate(),rentList.get(i).getReturnDate(),false,false));
                    }

                try {
                    //boolean isSaved = rentModel.saveRent(rents,rentDetails,payment);
                    boolean isSaved = addrentModel.saveRent(rents,rentDetails,payment);

                    if (isSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Successfully completed rent!").show();

                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Successfully completed rent!").show();
                }
            }
                else {
                    new Alert(Alert.AlertType.ERROR, "payment is essential").show();
                }

           // }

           // else {
               // new Alert(Alert.AlertType.ERROR, "Not avelible period").show();
          //  }

        }
        catch (RuntimeException e){
           //new Alert(Alert.AlertType.ERROR," have some Empty fields.. please filling them").show();

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

boolean isvalid= labelRentPrice.getText().isEmpty()||txtReturnDate.getValue()==null||txtRentDate.getValue()==null||
        lebalName.getText().isEmpty();
        if (!isvalid) {
            String dressId = dressComboBox.getValue();
           boolean doubleOrder=checkDoubleRent(dressId);
            System.out.println("double "+doubleOrder);

            boolean avalibility = checkDateAvelible(dressId);
            if(!doubleOrder) {
                if (!avalibility) {
                    checkDates.setSelected(false);
                    new Alert(Alert.AlertType.ERROR, "not available").show();

                }
            }
            if(doubleOrder){
                checkDates.setSelected(false);
                new Alert(Alert.AlertType.ERROR, "not available").show();
            }
        }
        else {
            new Alert(Alert.AlertType.ERROR, "Empty fields").show();
            checkDates.setSelected(false);
        }
    }

    private boolean checkDoubleRent(String dressId) {
        if (!rentList.isEmpty()) {
            for (int i = 0; i < cartTable.getItems().size(); i++) {
                if (colDressId.getCellData(i).equals(dressId)) {
                   // int col_qty = (int) colQty.getCellData(i);

                    boolean rent_date = txtRentDate.getValue().isAfter((ChronoLocalDate) colReservationDate.getCellData(i))
                            && txtRentDate.getValue().isBefore((ChronoLocalDate) colReturnDate.getCellData(i));
                    boolean rentE =txtRentDate.getValue().isEqual((ChronoLocalDate) colReservationDate.getCellData(i)) ||
                            txtRentDate.getValue().isEqual((ChronoLocalDate) colReturnDate.getCellData(i));
                    boolean return_date = txtReturnDate.getValue().isAfter((ChronoLocalDate) colReservationDate.getCellData(i))
                            && txtReturnDate.getValue().isBefore((ChronoLocalDate) colReturnDate.getCellData(i));
                    boolean rent_date1 =  ((ChronoLocalDate) colReservationDate.getCellData(i)).isAfter(txtRentDate.getValue())
                            && ((ChronoLocalDate) colReservationDate.getCellData(i)).isBefore(txtReturnDate.getValue());
                    boolean return_date1 =((ChronoLocalDate)colReturnDate.getCellData(i)).isAfter(txtRentDate.getValue())
                            && ((ChronoLocalDate) colReturnDate.getCellData(i)).isBefore(txtReturnDate.getValue());
                    if(rentE || rent_date || return_date ||rent_date1||return_date1){
                        return true;
                    }

                }
            }
        }
        return false;
    }

    private boolean checkDateAvelible(String dressId) throws ParseException, SQLException {
        System.out.println("goooo");
        LocalDate rent_date = txtRentDate.getValue();
        LocalDate return_date = txtReturnDate.getValue();


        List<RentDetailsDto> allRentals = rentDetailsModel.getAllRentals();
        boolean isAvealibility = rentModel.checkDateAvelibility(rent_date, return_date, allRentals,dressId);
        System.out.println(isAvealibility+"isavelible");
        //if(isAvealibility==false) {
            //new Alert(Alert.AlertType.ERROR,"Not avelibile date").show();
           // checkDates.setSelected(false);
        //}
        boolean valid=((rent_date.isAfter(LocalDate.now()))&&(return_date.isAfter(LocalDate.now())))||
                ((rent_date.isEqual(LocalDate.now())&&return_date.isAfter(LocalDate.now())));
        System.out.println(valid+"isValid");
       // boolean wrongDate=txtReturnDate.getValue().isBefore(txtRentDate.getValue());
        boolean correctDate=txtRentDate.getValue().isBefore(txtReturnDate.getValue());
        System.out.println("wrong Date"+correctDate);
        return (isAvealibility&&(valid)&&(correctDate));

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

    @FXML
    void invoiceButtonOnAction(ActionEvent event) throws JRException, SQLException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/rentInvoice.jrxml");
        JasperDesign load = JRXmlLoader.load(resourceAsStream);
        JasperReport compileReport = JasperCompileManager.compileReport(load);
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                        compileReport, //compiled report
                        null,
                        DbConnection.getInstance().getConnection() //database connection
                );
        JasperViewer.viewReport(jasperPrint, false);

    }
}
