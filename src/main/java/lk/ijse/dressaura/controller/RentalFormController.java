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
import lk.ijse.dressaura.dto.RentDto;
import lk.ijse.dressaura.dto.tm.RentTm;
import lk.ijse.dressaura.model.CustomerModel;
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
    private ObservableList<RentTm> obList = FXCollections.observableArrayList();
    @FXML
    void addButtonOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/add_rental_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
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
        int i=1;

        List<RentDto> rentList=rentModel.getAllRentals();
        CustomerModel cusModel=new CustomerModel();


        for (RentDto rent:rentList) {
            if(!rent.isReturn()){
            Button btnD=new Button("Delete");
            Button btnReturn=new Button("isReturn");
            CheckBox checkBox = new CheckBox();
            obList.add(new RentTm(rent.getRentId(),rent.getCusId(),cusModel.searchCustomer(rent.getCusId()).getName(),rent.getDressId(),cusModel.searchCustomer(rent.getCusId()).getContact(),String.valueOf(i),rent.getDate(),rent.getReturnDate(),btnD,btnReturn,checkBox));
            i++;
            deleteRentButtonOnAction(btnD,rentList);
            returnRentButtonOnAction(btnReturn,rentList);
            checkBoxOnAction(checkBox,rentList);
            btnD.setCursor(Cursor.HAND);
            btnReturn.setCursor(Cursor.HAND);
           checkBox.setSelected(rent.isReserve());
        }}
      rentViewSTable.setItems(obList);


    }

    private void checkBoxOnAction(CheckBox checkBox, List<RentDto> rentList) {
        checkBox.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure reservation Complete?", yes, no).showAndWait();
            if (type.orElse(no) == yes) {
                int focusedIndex =rentViewSTable.getSelectionModel().getSelectedIndex();

                checkBox.setSelected(true);
                rentViewSTable.refresh();

                try {
                    boolean isUpdated = rentModel.completeReservation(rentList.get(focusedIndex + 1).getRentId());
                    if(isUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION,"Successfully updated").show();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
          if(type.orElse(no) == no){
                checkBox.setSelected(false);
            }




        });

    }

    private void returnRentButtonOnAction(Button btnReturn, List<RentDto> rentList) {
        btnReturn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure return Complete?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int focusedIndex =rentViewSTable.getSelectionModel().getSelectedIndex();

                obList.remove(focusedIndex+1);
                rentViewSTable.refresh();
                try {
                    boolean isUpdated = rentModel.completeRent(rentList.get(focusedIndex + 1).getRentId());
                    if(isUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION,"Successfully updated").show();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }

    private void deleteRentButtonOnAction(Button btnD,List<RentDto> rentList) {

        btnD.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int focusedIndex =rentViewSTable.getSelectionModel().getSelectedIndex();

                obList.remove(focusedIndex+1);
                rentViewSTable.refresh();

                try {
                   if( rentModel.deleteRent(rentList.get(focusedIndex+1).getRentId())){
                       new Alert(Alert.AlertType.CONFIRMATION,"Successfully deleted").show();
                   }
                   new Alert(Alert.AlertType.ERROR,"Can not delete").show();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }


}
