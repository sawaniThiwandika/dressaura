package lk.ijse.dressaura.model;

import com.google.protobuf.StringValue;
import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.UserDto;
import lk.ijse.dressaura.sendEmail.Mail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
public class UserModel {
    static String userName;
    static String password;
    static String email;
    static String body;

    public static boolean checkUsername(UserDto loginDto) throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="select* from user";
        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet= pstm.executeQuery();
        while (resultSet.next()){
            userName=resultSet.getString(1);
            if("sawani".equals(loginDto.getUserName())){
                return false;
            }
        }
        return false;
    }
    public static boolean checkPassword(UserDto loginDto) throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="select* from user";
        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet= pstm.executeQuery();
        while (resultSet.next()){
            password=resultSet.getString(2);
            if("1234".equals(loginDto.getPassword())){
            return true;
        }

    }
        return false;
    }

    public static boolean checkCreditinal(UserDto loginDto) throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="select* from user";
        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet= pstm.executeQuery();
        while (resultSet.next()){
            userName=resultSet.getString("user_name");
            password=resultSet.getString("password");
            if(loginDto.getUserName().equals(userName)){
                if (loginDto.getPassword().equals(password)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkUsernameEmail(UserDto loginDto) throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="select* from user";
        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet= pstm.executeQuery();
        while (resultSet.next()){
            userName=resultSet.getString("user_name");
           email = resultSet.getString("email");
            if(loginDto.getUserName().equals(userName)){
                if (loginDto.getEmail().equals(email)){
                    Mail mail=new Mail();
                    String[] to={email};
                    String subject="forget password recovery";
                    Random rand = new Random();
                    body =String.valueOf(rand.nextInt(1000)) ;
                    mail.sendFromGMail(to, subject, body);
                    return true;
                }
            }
        }
    return false;
    }

    public static boolean checkRecovaryCode(String recoveryCode) {

        if(recoveryCode.equals(body)){return true;}
        else {return false;}
    }
}
