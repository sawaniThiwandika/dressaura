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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.RentDetailsDto;
import lk.ijse.dressaura.dto.RentDto;
import lk.ijse.dressaura.dto.tm.RentTm;
import lk.ijse.dressaura.model.CustomerModel;
import lk.ijse.dressaura.model.RentDetailsModel;
import lk.ijse.dressaura.model.RentModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class RentalFormController {
    @FXML
    private Button add;

    @FXML
    private AnchorPane rental;
    @FXML
    private Label NoOfRentalsS;

    @FXML
    private TableColumn<?, ?> colDeleteAction;

    @FXML
    private TableColumn<?, ?> colCusContact;

    @FXML
    private TableColumn<?, ?> colCusName;

    @FXML
    private TableColumn<?, ?> colDressId;

    @FXML
    private TableColumn<?, ?> colNum;

    @FXML
    private TableColumn<?, ?> colRentId;

    @FXML
    private TableColumn<?, ?> colReserveDate;

    @FXML
    private TableColumn<?, ?> colReturnDate;
    @FXML
    private TableColumn<?, ?> colReturnAction;

    @FXML
    private Label late;

    @FXML
    private Label ongoing;

    @FXML
    private TableColumn<?, ?> rentCusId;

    @FXML
    private TableView<RentTm> rentViewSTable;

    @FXML
    private AnchorPane rentalPane;
    @FXML
    private TableColumn<?, ?> colReservationAction;

    @FXML
    private Label upcoming;
    RentDto rentDto=new RentDto();
    RentModel rentModel=new RentModel();
    RentDetailsModel rentDetailsModel=new RentDetailsModel();
    private ObservableList<RentTm> obList = FXCollections.observableArrayList();
    @FXML
    void addButtonOnAction(ActionEvent event) throws IOException {
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
        deleteAllLateReservation();
        loadAllIncompletedRentals();
        setCellValueFactory();
    }

    private void deleteAllLateReservation() throws SQLException {
      rentModel.deleteAllLateReservations();

    }

    private void setCellValueFactory() {
        colNum.setCellValueFactory(new PropertyValueFactory<>("number"));
        rentCusId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCusContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colDressId.setCellValueFactory(new PropertyValueFactory<>("dressId"));
        colReserveDate.setCellValueFactory(new PropertyValueFactory<>("reserveDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colRentId.setCellValueFactory(new PropertyValueFactory<>("rentId"));
        colDeleteAction.setCellValueFactory(new PropertyValueFactory<>("delete_button"));
        colReturnAction.setCellValueFactory(new PropertyValueFactory<>("isreturn"));
        colReservationAction.setCellValueFactory(new PropertyValueFactory<>("reservation"));


    }

    private void loadAllIncompletedRentals() throws SQLException {

        List<RentDetailsDto> rentDetailsList=rentDetailsModel.getAllRentals();
        List<RentDto> rentList=rentModel.getAllRentals();
        CustomerModel cusModel=new CustomerModel();



        for(int i=0;i<rentDetailsList.size();i++){
            RentDto dto = rentModel.searchDetails(rentDetailsList.get(i).getRent_id());
            if(!rentDetailsList.get(i).isReturn()){
            Button btnD=new Button("Delete");
            Button btnReturn=new Button("isReturn");
            CheckBox checkBox = new CheckBox();

            System.out.println(dto.getCusId());
           // System.out.println(dto.getPaymentType());

            obList.add(new RentTm(rentDetailsList.get(i).getRent_id(),dto.getCusId(),cusModel.searchCustomer(dto.getCusId()).getName(),rentDetailsList.get(i).getDress_id(),
                    cusModel.searchCustomer(dto.getCusId()).getContact(),String.valueOf(i+1),rentDetailsList.get(i).getReservation_date(),rentDetailsList.get(i).getReturn_date(),
                    btnD,btnReturn,checkBox));

            deleteRentButtonOnAction(btnD,rentDetailsList);
            returnRentButtonOnAction(btnReturn,rentDetailsList);
            checkBoxOnAction(checkBox,rentDetailsList);
            btnD.setCursor(Cursor.HAND);
            btnReturn.setCursor(Cursor.HAND);
           checkBox.setSelected(rentDetailsList.get(i).isReserve());
        }}
      rentViewSTable.setItems(obList);


    }

    private void checkBoxOnAction(CheckBox checkBox, List<RentDetailsDto> rentDetailsList) {
        checkBox.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            RentTm rentTm = rentViewSTable.getSelectionModel().getSelectedItem();

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure reservation Complete?", yes, no).showAndWait();
            if (type.orElse(no) == yes) {


                int focusedIndex =rentViewSTable.getSelectionModel().getSelectedIndex();
                System.out.println("forcued INdex"+ focusedIndex);


                System.out.println("rent id"+ (String) colDressId.getCellData(focusedIndex));
                System.out.println("rent id"+ (String) colRentId.getCellData(focusedIndex));
                try {
                  // boolean isComplete = rentDetailsModel.checkPaymentComplete(String.valueOf(colRentId.getCellData(focusedIndex)));
                    boolean isComplete = rentDetailsModel.checkPaymentComplete(rentTm.getRentId());
                    if(isComplete){
                        checkBox.setSelected(true);
                        rentViewSTable.refresh();
                    //boolean isUpdated = rentDetailsModel.completeReservation(String.valueOf(colRentId.getCellData(focusedIndex)),
                            //String.valueOf(colDressId.getCellData(focusedIndex)));
                        boolean isUpdated = rentDetailsModel.completeReservation(rentTm.getRentId(), rentTm.getDressId());
                    if(isUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION,"Successfully updated").show();

                    }
                }
                    else{
                        ButtonType yes1 = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                        ButtonType no1 = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                        Optional<ButtonType> type1 = new Alert(Alert.AlertType.INFORMATION, "Is payment completed?", yes, no).showAndWait();

                        if (type1.orElse(no) == yes) {
                            checkBox.setSelected(true);
                            updateRentDetails(rentTm.getRentId(),rentTm.getDressId(), checkBox);

                        }
                        else
                        {checkBox.setSelected(false); }


                    }
                }catch (SQLException ex){
                        throw new RuntimeException(ex);

                }
            }
          if(type.orElse(no) == no){
                checkBox.setSelected(false);
            }




        });

    }

    private void updateRentDetails(String rentId, String dressId, CheckBox checkBox) throws SQLException {
        checkBox.setSelected(true);
        rentViewSTable.refresh();
        boolean isUpdated = rentDetailsModel.completeReservation(rentId,dressId);

        if(isUpdated){
            new Alert(Alert.AlertType.CONFIRMATION,"Successfully updated").show();
    }}

    private void returnRentButtonOnAction(Button btnReturn, List<RentDetailsDto> rentDetailsList) {
        btnReturn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure return Complete?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int focusedIndex =rentViewSTable.getSelectionModel().getSelectedIndex();
                RentTm rentTm = rentViewSTable.getSelectionModel().getSelectedItem();
                obList.remove(focusedIndex);
                rentViewSTable.refresh();
                try {
                    boolean isUpdated = rentDetailsModel.completeRent(rentTm.getRentId(), rentTm.getDressId());
                    if(isUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION,"Successfully updated").show();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }

    private void deleteRentButtonOnAction(Button btnD, List<RentDetailsDto> rentList) {

        btnD.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int focusedIndex =rentViewSTable.getSelectionModel().getSelectedIndex();
                RentTm rentTm = rentViewSTable.getSelectionModel().getSelectedItem();
                obList.remove(focusedIndex);
                rentViewSTable.refresh();
                System.out.println("aaaaa"+(String)colRentId.getCellData(focusedIndex)+"  "+(String)colDressId.getCellData(focusedIndex));

                try {
                    if( rentDetailsModel.deleteRent(rentTm.getRentId(),rentTm.getDressId())){
                        new Alert(Alert.AlertType.CONFIRMATION,"Successfully deleted").show();
                        rentViewSTable.refresh();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                new Alert(Alert.AlertType.ERROR,"Can not delete").show();

            }
        });

    }


}
