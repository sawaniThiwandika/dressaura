package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.EmployeeDto;
import lk.ijse.dressaura.dto.SupplierDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
boolean isDuplicate=false;
    public boolean addEmployee(EmployeeDto dto) throws SQLException {
        List<EmployeeDto> employeeTableValues = getEmployeeTableValues();
        for (int i=0;i<employeeTableValues.size();i++){
            if(dto.getEmpId().equals(employeeTableValues.get(i).getEmpId())){
                //updateEmployee();
            isDuplicate= true;
            }

            else {
                isDuplicate=false;
            }


        }




if(!isDuplicate){
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            String sql = "INSERT INTO employee VALUES(?, ?, ?, ?,?,?,?)";
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setString(1, dto.getEmpId());
            pstm.setString(2, dto.getName());
            pstm.setString(3, dto.getAddress());
            pstm.setString(4, dto.getContact());
            pstm.setString(5, dto.getEmail());
            pstm.setString(6, dto.getUserName());
            pstm.setString(7, dto.getJobRole());

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

else {
    Connection connection = null;
    try {
        connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        String sql = "UPDATE employee SET  name = ?, address = ? ,contact=? ,email=? ,jobRole=?WHERE emp_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(6, dto.getEmpId());
        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getAddress());
        pstm.setString(3, dto.getContact());
        pstm.setString(4, dto.getEmail());
        pstm.setString(5, dto.getJobRole());

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
    }
    public String generateNextEmployeeId() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT emp_id FROM employee ORDER BY emp_id DESC LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return splitEmployeeId(resultSet.getString(1));
        }
        return splitEmployeeId(null);
    }
    private String splitEmployeeId(String currentEmployeeId){
            if (currentEmployeeId != null) {
                String[] split = currentEmployeeId.split("E0");
                int id = Integer.parseInt(split[1]); //01
                id++;
                return "E00" + id;
            } else {
                return "E001";
            }

    }

    public List<EmployeeDto> getEmployeeTableValues() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT *FROM employee ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        ArrayList<EmployeeDto> employeeList=new ArrayList<>();

        while(resultSet.next()) {employeeList.add(new EmployeeDto(
                resultSet.getString(2),
                resultSet.getString(1),
                resultSet.getString(5),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(6),
                resultSet.getString(7)
                ));
        }
        return employeeList;

    }
}
