package lk.ijse.dressaura.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

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

import java.util.List;
import java.util.ResourceBundle;

public class DressFormController implements Initializable {



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
    public void initialize(URL view, ResourceBundle add_dress_form) {


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
                smallController.getName().setText(dtoList.get(i).getName());
                smallController.getDressID().setText(dtoList.get(i).getDressId());
                smallController.getDressSize().setText(dtoList.get(i).getSize());
                //smallController.getDressAvelibity().setText(dtoList.get(i).getAvelability()==true?"Available":"Not Available");
                smallController.getDressAvelibity().setText(aveliblity==true?"Available":"Not Available");
                if(x/7==1){x=0;y=y+1;}
                gridPane.add(smallPane, x, y);
                x++;


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

