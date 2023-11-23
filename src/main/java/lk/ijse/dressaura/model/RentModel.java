package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.RentDetailsDto;
import lk.ijse.dressaura.dto.RentDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentModel {
    public String generateNextId() throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="SELECT rent_id FROM rent ORDER BY rent_id DESC LIMIT 1";
        PreparedStatement pstm= connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            return spiltRentalId(resultSet.getString(1));

        }
        return spiltRentalId(null);

    }
    public String spiltRentalId(String currentRentalId){
        if(currentRentalId!=null){
            String [] split= currentRentalId.split("R0");
            int id=Integer.parseInt(split[1]);
            id++;
            return "R00"+id;
        }
        else {
            return "R001";
        }

    }


    public List<RentDto> getAllRentals() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="SELECT* FROM rent";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        List <RentDto> rentals=new ArrayList<>();
        while (resultSet.next()){
            rentals.add(
            new RentDto(
                    resultSet.getString("rent_id"),
                    resultSet.getString("cus_id"),
                    resultSet.getString("pay_id"),
                    resultSet.getDate("date").toLocalDate(),
                    resultSet.getBoolean("payment_complete")

            )
            );
            System.out.println( resultSet.getString("rent_id")+" "+ resultSet.getString("cus_id")+" "+
                    resultSet.getString("pay_id")+ " " + resultSet.getDate("date").toLocalDate() );
        }
        return rentals;
    }
    public RentDto searchDetails(String rentId) throws SQLException {
        System.out.println(rentId+"abc");
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="select* from rent where rent_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,rentId);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            RentDto dto = new RentDto(
                    resultSet.getString("rent_id"),
                    resultSet.getString("cus_id"),
                    resultSet.getString("pay_id"),
                    resultSet.getDate("date").toLocalDate(),
                    resultSet.getBoolean("payment_complete")
            );
            return dto;
        }
        return null;
    }


    public boolean checkDateAvelibility(LocalDate rentDate, LocalDate returnDate, List<RentDetailsDto> allRentals,
                                        String dressId) {
        RentDetailsModel model=new RentDetailsModel();
        for (int i = 0; i < allRentals.size(); i++) {
            if(dressId.equals(allRentals.get(i).getDress_id())){
                boolean wrongDate=returnDate.isBefore(rentDate);
            boolean rent_date = rentDate.isAfter(allRentals.get(i).getReservation_date()) &&
                    rentDate.isBefore(allRentals.get(i).getReturn_date());
            boolean rentE = rentDate.isEqual(allRentals.get(i).getReservation_date()) ||
                    rentDate.isEqual(allRentals.get(i).getReturn_date());
            boolean return_date = returnDate.isAfter(allRentals.get(i).getReservation_date()) &&
                    returnDate.isBefore(allRentals.get(i).getReturn_date());
            boolean returnE = returnDate.isEqual(allRentals.get(i).getReservation_date()) ||
                    returnDate.isEqual(allRentals.get(i).getReturn_date());
            boolean rent_date1 = allRentals.get(i).getReservation_date().isAfter(rentDate) &&
                    allRentals.get(i).getReturn_date().isBefore(returnDate);
            boolean return_date1 = allRentals.get(i).getReturn_date().isAfter(rentDate) &&
                    allRentals.get(i).getReturn_date().isBefore(returnDate);
          if(rentE || rent_date || return_date || returnE||rent_date1||return_date1||wrongDate){
              return false;
          }

        }
        }
        return true;

    }




   public boolean addRent(RentDto rents) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            String sql = "INSERT INTO rent VALUES(?, ?, ?,?,?)";
            PreparedStatement pstm = connection.prepareStatement(sql);
            System.out.println("i");
            pstm.setString(1, rents.getRentId());
            pstm.setString(2, rents.getCusId());
            pstm.setString(3, rents.getPayId());
            pstm.setDate(4, Date.valueOf(rents.getDate()));
            pstm.setBoolean(5, rents.isPaymentType());
            System.out.println(rents.getRentId() + rents.getCusId());
            System.out.println(rents.getDate());
          //  System.out.println(rents.getPaymentType());
            System.out.println(rents.getPayId());
            boolean isSaved = pstm.executeUpdate() > 0;
            return isSaved;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
   }


    public boolean completeRent(String rentId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql="UPDATE rent SET isreturn=? WHERE rent_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setBoolean(1,true);
        pstm.setString(2,rentId);

        return pstm.executeUpdate()>0;


    }

    public boolean deleteRent(String rentId, String dressId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        RentDetailsModel rentDetailsModel=new RentDetailsModel();
        boolean deleted = rentDetailsModel.deleteRent(rentId, dressId);
        if(deleted)
        { String sql="DELETE FROM rent WHERE rent_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,rentId);
        return pstm.executeUpdate()>0;}
        return false;
    }

    public void deleteAllLateReservations() throws SQLException {
        RentDetailsModel rentDetailsModel=new RentDetailsModel();
        List<RentDetailsDto> allRentals =rentDetailsModel.getAllRentals();
        //List<RentDetailsDto> lateReservations=new ArrayList<>();
        for(RentDetailsDto rent:
        allRentals){
            if(rent.isReserve()==false&&LocalDate.now().isAfter(rent.getReservation_date())){
             deleteRent(rent.getRent_id(),rent.getDress_id());

            }
        }



    }



    public String genatateNewRentIdForMoreRents(String text) {
      return   spiltRentalId(text);

    }

    public boolean completePayment(String rentId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="UPDATE rent SET payment_complete=? WHERE rent_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setBoolean(1,true);
        pstm.setString(2,rentId);

        return pstm.executeUpdate()>0;
    }
}

