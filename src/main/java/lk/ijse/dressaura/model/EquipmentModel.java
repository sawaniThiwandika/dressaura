package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.EmployeeDto;
import lk.ijse.dressaura.dto.EquipmentDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentModel {
    boolean isDuplicate=false;



    public String generateNextId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT equipment_id FROM equipment ORDER BY equipment_id DESC LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return splitEquipmentId(resultSet.getString(1));
        }
        return splitEquipmentId(null);
    }

    private String splitEquipmentId(String currentEquipmentId) {
        if(currentEquipmentId != null) {
            String[] split = currentEquipmentId.split("EQ0");

            int id = Integer.parseInt(split[1]);
            id++;
            if (id<10){
                return "EQ00" + id;}
            else {
                return "EQ0"+id;
            }
        } else {
            return "EQ001";
        }

    }

    public boolean saveEquipment(EquipmentDto dto) throws SQLException {


        isDuplicate = checkDuplicates(dto);

        if (!isDuplicate) {
            Connection connection = null;
            try {
                connection = DbConnection.getInstance().getConnection();
                connection.setAutoCommit(false);

                String sql = "INSERT INTO equipment VALUES (?,?,?,?,?,?)";
                PreparedStatement pstm = connection.prepareStatement(sql);
                pstm.setString(1, dto.getEquipmentId());
                pstm.setString(2, dto.getName());
                pstm.setDouble(3, dto.getPrice());
                pstm.setDate(4, Date.valueOf(dto.getDate()));
                pstm.setString(5, dto.getUserName());
                pstm.setString(6, dto.getDescription());

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
                String sql = "UPDATE equipment SET  name = ?, description = ? ,price=? ,date=? WHERE equipment_id = ?";
                PreparedStatement pstm = connection.prepareStatement(sql);

                pstm.setString(1, dto.getName());
                pstm.setString(2, dto.getDescription());
                pstm.setDouble(3, dto.getPrice());
                pstm.setDate(4, Date.valueOf(dto.getDate()));
                pstm.setString(5, dto.getEquipmentId());

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

    private boolean checkDuplicates(EquipmentDto dto) throws SQLException {
        List<EquipmentDto> equipmentDtos = getEquipmentTableValues();
        for (int i=0;i<equipmentDtos.size();i++){
            if(dto.getEquipmentId().equals(equipmentDtos.get(i).getEquipmentId())){
                //updateEmployee();
                return true;
            }

        }
        return false;
    }

    public List<EquipmentDto> getEquipmentTableValues() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM  equipment ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        ArrayList<EquipmentDto> dtoList = new ArrayList<>();

        while(resultSet.next()) {
            dtoList.add(
                    new EquipmentDto(
                            resultSet.getString(1),
                            resultSet.getString(5),
                            resultSet.getString(2),
                            resultSet.getDouble(3),
                            resultSet.getDate(4).toLocalDate(),
                            resultSet.getString(6)

                    )
            );
        }
        return dtoList;
    }

    public boolean deleteEmployee(String id) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        String sql= "DELETE FROM equipment WHERE equipment_id=?";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1, id);
        return  pstm.executeUpdate()>0;
    }
}
