package lk.ijse.dressaura.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dressaura.dto.DressDto;
import lk.ijse.dressaura.model.DressModel;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DressFormController implements Initializable {

    @FXML
    private ComboBox<String> filterCombo;

    @FXML
    private VBox smallPanes;

    @FXML
    private AnchorPane bigDresssPane;

    static DressModel dressModel=new DressModel();
    @FXML
    private Button add;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label numOfDress;




    @FXML
    void addButtonOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/add_dress_form.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent load = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Add dress");
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL view, ResourceBundle add_dress_form)  {
        setComboBoxValues();
        try {
            updateLabels();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        int y=0; int x=0;
        List<DressDto> dtoList = null;


        try {
            dtoList = dressModel.getAllDress();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        boolean aveliblity=true;

        for (int i = 0; i <dtoList.size(); i++) {
            try {
               aveliblity = dressModel.checkAvelibityOfDress(dtoList.get(i).getDressId());

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/one_dress_form.fxml"));
                Parent smallPane = loader.load();
                OneDressForController smallController = loader.getController();
                smallController.setDressDetails(dtoList.get(i));
                smallController.getName().setText(dtoList.get(i).getName());
                smallController.getDressID().setText(dtoList.get(i).getDressId());
                smallController.getDressSize().setText(dtoList.get(i).getSize());
                Image image = new Image("file:" + dtoList.get(i).getPhotoPath());
                smallController.getDressImage().setImage(image);
                smallController.getDressAvelibity().setText(aveliblity==true?"Available":"Not Available");
                if(x/4==1){x=0;y=y+1;}
                gridPane.add(smallPane, x, y);
                x++;


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void initialize(List<DressDto> dtoList){
        gridPane.getChildren().clear();
        int y=0; int x=0;

        boolean aveliblity=true;

        for (int i = 0; i <dtoList.size(); i++) {
            try {
                aveliblity = dressModel.checkAvelibityOfDress(dtoList.get(i).getDressId());

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/one_dress_form.fxml"));
                Parent smallPane = loader.load();
                OneDressForController smallController = loader.getController();
                smallController.setDressDetails(dtoList.get(i));
                smallController.getName().setText(dtoList.get(i).getName());
                smallController.getDressID().setText(dtoList.get(i).getDressId());
                smallController.getDressSize().setText(dtoList.get(i).getSize());
                Image image = new Image("file:" + dtoList.get(i).getPhotoPath());
                smallController.getDressImage().setImage(image);
                smallController.getDressAvelibity().setText(aveliblity==true?"Available":"Not Available");
                if(x/4==1){x=0;y=y+1;}
                gridPane.add(smallPane, x, y);
                x++;


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setComboBoxValues() {
        ObservableList<String> types = FXCollections.observableArrayList("All","Frock", "Saree", "Lehenga",
                "Salwar Kameez", "Kurta");
        filterCombo.setItems(types);


    }

    private void updateLabels() throws SQLException {
        List<DressDto> allDress = dressModel.getAllDress();
        numOfDress.setText(String.valueOf(allDress.size()));
    }

    @FXML
    void comboFilterOnAction(ActionEvent event) throws SQLException {
        List<DressDto> frocks = new ArrayList<>();
        List<DressDto> saree = new ArrayList<>();
        List<DressDto> lehenga = new ArrayList<>();
        List<DressDto> salwar = new ArrayList<>();
        List<DressDto> kurtha = new ArrayList<>();
        List<DressDto> allDress = dressModel.getAllDress();
        for(int i=0;i<allDress.size();i++){
            String type = allDress.get(i).getType();
            DressDto dto= allDress.get(i);

            if(type.equals("Frock")){
                frocks.add(dto);
            }
            else if(type.equals("Lehenga")){
                lehenga.add(dto);
            } else if (type.equals("Kurta")) {
                kurtha.add(dto);
            } else if (type.equals("Salwar Kameez")) {
                salwar.add(dto);
            } else if (type.equals("Saree")) {
                saree.add(dto);
            }

        }
        String value = filterCombo.getValue();
        if(value.equals("All")){
            initialize(allDress);
        }
        if (value.equals("Saree")){
            initialize(saree);
        }
        if(value.equals("Frock")){
            initialize(frocks);
        }
        if(value.equals("Salwar Kameez")){
            initialize(salwar);
        }
        if (value.equals("Lehenga")){
            initialize(lehenga);

        }
    }
}

