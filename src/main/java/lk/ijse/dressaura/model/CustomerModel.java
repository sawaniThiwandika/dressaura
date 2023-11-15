package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.CustomerDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {
    public boolean saveCustomer(CustomerDto dto) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            String sql = "INSERT INTO customer VALUES(?, ?, ?, ?,?)";
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setString(1, dto.getCusId());
            pstm.setString(2, dto.getName());
            pstm.setString(3, dto.getAddress());
            pstm.setString(4, dto.getContact());
            pstm.setString(5, dto.getEmail());

            boolean isSaved = pstm.executeUpdate() > 0;
            if (isSaved) {
                connection.commit();
            }
            connection.rollback();


        } finally {
            connection.setAutoCommit(true);
        }
        return true;
    }

    public String generateNextCustomerId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT cus_id FROM customer ORDER BY cus_id DESC LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return splitCustomerId(resultSet.getString(1));
        }
        return splitCustomerId(null);
    }
    private String splitCustomerId(String currentCustomerId) {
        if(currentCustomerId != null) {
            String[] split = currentCustomerId.split("C0");
            int id = Integer.parseInt(split[1]);
            id++;
            return "C00" + id;
        } else {
            return "C001";
        }
    }

    public List<CustomerDto> getCustomerTableValues() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT *FROM customer ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
     ArrayList<CustomerDto> customerList=new ArrayList<>();

        while(resultSet.next()) {customerList.add(new CustomerDto(
                resultSet.getString(2),
                resultSet.getString(5),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(1)));
        }
        return customerList;
    }

    public void deleteCustomer(int focusedIndex) throws SQLException {

    }

    public ArrayList<CustomerDto> getCustomer(String cusId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT *FROM customer WHERE cus_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, cusId);
        ResultSet resultSet = pstm.executeQuery();
        ArrayList<CustomerDto> customerList=new ArrayList<>();
        while (resultSet.next()) {
            customerList.add(
            new CustomerDto(
                    resultSet.getString(2),
                    resultSet.getString(5),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(1)
            ));

        }
        return customerList;
    }

    public boolean updateCustomer(CustomerDto dto) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            String sql = "UPDATE customer SET  name = ?, address = ? ,contact=? ,email=? WHERE cus_id = ?";
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setString(1, dto.getName());
            pstm.setString(2, dto.getAddress());
            pstm.setString(3, dto.getContact());
            pstm.setString(4, dto.getEmail());
            pstm.setString(5, dto.getCusId());

            boolean isSaved = pstm.executeUpdate() > 0;
            System.out.println(isSaved);
            if (isSaved) {
                System.out.println(isSaved);
                connection.commit();
            }
            connection.rollback();

        } finally {
            connection.setAutoCommit(true);
        }
        return true;
    }


    public CustomerDto searchCustomer(String id) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        String sql= "SELECT * FROM customer WHERE cus_id=?";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            CustomerDto customer=new CustomerDto( resultSet.getString(2),
                    resultSet.getString(5),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(1));
            return customer;
        }
        else{
            return  null;
        }
    }

    public List<CustomerDto> getAllCustomer() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM customer";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        ArrayList<CustomerDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            dtoList.add(
                    new CustomerDto(
                            resultSet.getString(2),
                            resultSet.getString(5),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(1)
                    )
            );
        }
        return dtoList;
    }
}

