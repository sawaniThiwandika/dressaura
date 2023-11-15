package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.CustomerDto;
import lk.ijse.dressaura.dto.DressDto;
import lk.ijse.dressaura.dto.RentDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DressModel {
    public static boolean addDress(DressDto dto) throws SQLException {
        Connection connection=null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            String sql = "INSERT INTO dress VALUES(?, ?, ?, ?,?, ?, ?, ?,?)";
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setString(1, dto.getDressId());
            pstm.setString(2, dto.getName());
            pstm.setString(4, dto.getSize());
            pstm.setString(3, dto.getType());
            pstm.setDate(9,Date.valueOf(dto.getDate()));
            pstm.setString(8, dto.getUserName());
            pstm.setBoolean(6, dto.getAvelability());
            pstm.setDouble(5, dto.getRentPrice());
            pstm.setBlob(7, (Blob) null);
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

    public static DressDto searchDress(String id) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        String sql= "SELECT * FROM dress WHERE dress_id=?";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            DressDto dress=new DressDto(
                    resultSet.getString(8),
                    resultSet.getString(2),
                    resultSet.getString(1),
                    resultSet.getString(4),
                    resultSet.getDouble(5),
                    resultSet.getBoolean(6),
                    resultSet.getBlob(7),
                    resultSet.getString(8));
            return dress;
        }
        else{
            return  null;
    }
    }
    public String generateNextDressId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT dress_id FROM dress ORDER BY dress_id DESC LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return splitDressId(resultSet.getString(1));
        }
        return splitDressId(null);
    }
    private String splitDressId(String currentDressId) {
        if(currentDressId != null) {
            String[] split = currentDressId.split("D0");
            int id = Integer.parseInt(split[1]);
            id++;
            return "D00" + id;
        } else {
            return "D001";
        }
    }

    public List<DressDto> getAllDress() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM dress ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        ArrayList<DressDto> dtoList = new ArrayList<>();

        while(resultSet.next()) {
            dtoList.add(
                    new DressDto(
                            resultSet.getString(8),
                            resultSet.getString(2),
                            resultSet.getString(1),
                            resultSet.getString(4),
                            resultSet.getDouble(5),
                            resultSet.getBoolean(6),
                            resultSet.getBlob(7),
                            resultSet.getString(8)
                          // resultSet.getDate(9).toLocalDate()
                    )
            );
        }
        return dtoList;
    }

    public boolean updateDress(String dressId) throws SQLException {
        if (checkAvelibityOfDress(dressId)) {

            Connection connection = DbConnection.getInstance().getConnection();
            String sql = "UPDATE dress SET  avalibility=? WHERE dress_id = ?";
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setBoolean(1, false);
            pstm.setString(2, dressId);

            return (pstm.executeUpdate() > 0);

        }
        return true;
    }

    public boolean checkAvelibityOfDress(String dressId) throws SQLException {
        RentModel rent=new RentModel();
        List<RentDto> allRentals = rent.getAllRentals();
        for(RentDto dto:allRentals){
            LocalDate date=LocalDate.now();
            if(dto.getDressId().equals(dressId)){
                if((date.isBefore(dto.getReturnDate())&&(date.isAfter(dto.getDate())))||(date.isEqual(dto.getDate())||date.isEqual(dto.getReturnDate()))){
                    return false;
                }
            }
        }
        return true;
    }
}
