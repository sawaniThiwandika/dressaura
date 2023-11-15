package lk.ijse.dressaura.dto.tm;

import javafx.scene.control.Button;

public class CustomerTm {
    private String cusId;
    private String name;
    private String address;
    private String contact;
    private String number;

    public CustomerTm(int i, String supId, String name, String contact, String email, Button btnU, Button btnD) {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private String email;
    private Button delete_button;
    private Button update_button;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Button getDelete_button() {
        return delete_button;
    }

    public void setDelete_button(Button delete_button) {
        this.delete_button = delete_button;
    }

    public Button getUpdate_button() {
        return update_button;
    }

    public void setUpdate_button(Button update_button) {
        this.update_button = update_button;
    }



    public CustomerTm(int i,String cusId, String name, String address, String contact, String email, Button update,Button delete) {
        this.cusId = cusId;
        this.name = name;
        this.address = address;
        this.email = email;
        this.contact=contact;
        this.delete_button=delete;
        this.update_button=update;
        this.number= String.valueOf(i);

    }
}

