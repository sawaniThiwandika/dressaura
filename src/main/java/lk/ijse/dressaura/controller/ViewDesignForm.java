package lk.ijse.dressaura.controller;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class ViewDesignForm {
    @FXML
    private ImageView viewImage;

    public void setValues(String path) {
        Image image = new Image("file:" + path);
        viewImage.setImage(image);
    }
}
