package lk.ijse.dressaura.dto;

import java.sql.Blob;
import java.time.LocalDate;

public class DressDto {
    private String userName;
    private String name;
    private String dressId;
    private String size;
    private double rentPrice;
    private boolean avelability;
    private String type;
    private  LocalDate date;
    private  String photoPath;
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDressId() {
        return dressId;
    }

    public void setDressId(String dressId) {
        this.dressId = dressId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public boolean getAvelability() {
        return avelability;
    }

    public void setAvelability(boolean avelability) {
        this.avelability = avelability;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setPhotoPath(String photoPath){
        this.photoPath=photoPath;

    }
    public String getPhotoPath(){
        return photoPath;
    }

    public DressDto(String userName, String name, String dressId, String size, double  rentPrice, boolean avelability,
                    String path, String type, LocalDate date) {
        this.userName = userName;
        this.name = name;
        this.dressId = dressId;
        this.size = size;
        this.rentPrice = rentPrice;
        this.avelability = avelability;
        this.type = type;
        this.date=date;
        this.photoPath=path;
    }
    public DressDto(String userName, String name, String dressId, String size, double  rentPrice, boolean avelability, String path, String type) {
        this.userName = userName;
        this.name = name;
        this.dressId = dressId;
        this.size = size;
        this.rentPrice = rentPrice;
        this.avelability = avelability;
        this.type = type;
        this.photoPath=path;
    }
}
