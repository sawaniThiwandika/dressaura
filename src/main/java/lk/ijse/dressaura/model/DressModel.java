package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.DressDto;
import lk.ijse.dressaura.dto.RentDetailsDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DressModel {
    public  boolean addDress(DressDto dto) throws SQLException {
        boolean check = checkPreviousDressId(dto.getDressId());
        if(check){
            boolean isadd = updateDress(dto);
            return isadd;
        }

        else {

            Connection connection = null;
            try {
                connection = DbConnection.getInstance().getConnection();
                connection.setAutoCommit(false);
                String sql = "INSERT INTO dress VALUES(?, ?, ?, ?,?, ?, ?, ?,?)";
                PreparedStatement pstm = connection.prepareStatement(sql);

                pstm.setString(1, dto.getDressId());
                pstm.setString(2, dto.getName());
                pstm.setString(4, dto.getSize());
                pstm.setString(3, dto.getType());
                pstm.setDate(9, Date.valueOf(dto.getDate()));
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
    }

    private boolean updateDress(DressDto dto) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        String sql = "UPDATE dress SET   name = ? ,type=? ,size=? ,rent_price=?,avalibility=?,user_name=?," +
                "date=? WHERE dress_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);


        pstm.setString(8, dto.getDressId());
        pstm.setString(1, dto.getName());
        pstm.setString(3, dto.getSize());
        pstm.setString(2, dto.getType());
        pstm.setDate(7, Date.valueOf(dto.getDate()));
        pstm.setString(6, dto.getUserName());
        pstm.setBoolean(5, dto.getAvelability());
        pstm.setDouble(4, dto.getRentPrice());

        return pstm.executeUpdate() > 0;

    }

    private static boolean checkPreviousDressId(String dressId) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        String sql="select dress_id FROM dress GROUP BY  dress_id";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()){
            if(resultSet.getString(1).equals(dressId)){
                return true;

            }
        }
        return false;
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
                            resultSet.getString(3),
                            resultSet.getDate(9).toLocalDate()
                    )
            );
        }
        return dtoList;
    }

    public boolean updateDress(List<RentDetailsDto> rents) throws SQLException {
        for (int i = 0; i < rents.size(); i++) {
            if (checkAvelibityOfDress(rents.get(i).getDress_id())){

                Connection connection = DbConnection.getInstance().getConnection();
                String sql = "UPDATE dress SET  avalibility=? WHERE dress_id = ?";
                PreparedStatement pstm = connection.prepareStatement(sql);
                pstm.setBoolean(1, false);
                pstm.setString(2, rents.get(i).getDress_id());

                return (pstm.executeUpdate() > 0);

            }

        }
        return true;
    }

    public boolean checkAvelibityOfDress(String dressId) throws SQLException {
        RentDetailsModel rent=new RentDetailsModel();
        List<RentDetailsDto> allRentals = rent.getAllRentals();
        for(RentDetailsDto dto:allRentals){
            LocalDate date=LocalDate.now();
            if(dto.getDress_id().equals(dressId)){
                if((date.isBefore(dto.getReturn_date())&&(date.isAfter(dto.getReservation_date())))||
                        (date.isEqual(dto.getReservation_date())||date.isEqual(dto.getReturn_date()))){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean deleteDress(DressDto dto) throws SQLException {
        RentDetailsModel model=new RentDetailsModel();



        Connection connection = null;
        try {

            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            if(checkHaveRentals(dto.getDressId())){
            boolean deleteRent = model.deleteRentals(dto);
            System.out.println("Delete"+dto.getDressId());
            if(deleteRent) {
                System.out.println("Delete"+dto.getDressId());
                String sql = "DELETE FROM dress WHERE dress_id=?";
                PreparedStatement pstm = connection.prepareStatement(sql);

                pstm.setString(1, dto.getDressId());

                boolean isDeleted = pstm.executeUpdate() > 0;

                if (isDeleted) {
                    connection.commit();
                }
            }
                connection.rollback();


            }
            else{
                String sql = "DELETE FROM dress WHERE dress_id=?";
                PreparedStatement pstm = connection.prepareStatement(sql);

                pstm.setString(1, dto.getDressId());

                boolean isDeleted = pstm.executeUpdate() > 0;

                if (isDeleted) {
                    connection.commit();
                }
            }
            connection.rollback();



        }


        finally{
                connection.setAutoCommit(true);
            }

        return true;
    }

    private boolean checkHaveRentals(String dressId) throws SQLException {
        RentDetailsModel model=new RentDetailsModel();
        List<RentDetailsDto> everyRentals = model.getEveryRentals();
        for(int i=0;i<everyRentals.size();i++){
            if(everyRentals.get(i).getDress_id().equals(dressId)){
                return  true;
            }
        }
        return false;
    }

}
