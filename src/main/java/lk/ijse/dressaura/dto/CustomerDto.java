package lk.ijse.dressaura.dto;

public class CustomerDto {
    private String name;

    public CustomerDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public CustomerDto(String name, String email, String address, String contact, String cusId) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.cusId = cusId;
    }

    private String email;
    private String address;
    private String contact;
    private String cusId;

}
