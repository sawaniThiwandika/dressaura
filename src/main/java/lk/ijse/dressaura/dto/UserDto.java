package lk.ijse.dressaura.dto;

import java.awt.*;

public class UserDto {
    private String userName;
    private String password;
    private String email;

    public UserDto(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }
public UserDto(){

}

    public UserDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }



    @Override
    public String toString() {
        return "UserDto{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
