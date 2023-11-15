package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.PaymentDto;
import lk.ijse.dressaura.dto.RentDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

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
                    resultSet.getDate("reservation_date").toLocalDate(),
                    resultSet.getDate("return_date").toLocalDate(),
                    resultSet.getInt("no_of_days"),
                    resultSet.getString("dress_id"),
                    resultSet.getString("cus_id"),
                    resultSet.getString("pay_id"),
                    resultSet.getBoolean("isreturn"),
                    resultSet.getBoolean("isreserve")

            ));
        }
        return rentals;
    }


    public boolean checkDateAvelibility(LocalDate rentDate, LocalDate returnDate, List<RentDto> allRentals, String dressId) {
        for (int i = 0; i < allRentals.size(); i++) {
            if(dressId.equals(allRentals.get(i).getDressId())){
                boolean wrongDate=returnDate.isBefore(rentDate);
            boolean rent_date = rentDate.isAfter(allRentals.get(i).getDate()) && rentDate.isBefore(allRentals.get(i).getReturnDate());
            boolean rentE = rentDate.isEqual(allRentals.get(i).getDate()) || rentDate.isEqual(allRentals.get(i).getReturnDate());
            boolean return_date = returnDate.isAfter(allRentals.get(i).getDate()) && returnDate.isBefore(allRentals.get(i).getReturnDate());
            boolean returnE = returnDate.isEqual(allRentals.get(i).getDate()) || returnDate.isEqual(allRentals.get(i).getReturnDate());
            boolean rent_date1 = allRentals.get(i).getDate().isAfter(rentDate) && allRentals.get(i).getDate().isBefore(returnDate);
            boolean return_date1 = allRentals.get(i).getReturnDate().isAfter(rentDate) && allRentals.get(i).getReturnDate().isBefore(returnDate);
          if(rentE || rent_date || return_date || returnE||rent_date1||return_date1){
              return false;
          }

        }
        }
        return true;

    }




    public boolean saveRent(RentDto rent, PaymentDto payment) throws SQLException {
        PaymentModel payModel=new PaymentModel();
        DressModel dressModel=new DressModel();
        Connection connection = null;

        try {
            System.out.println("go");
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean  isSavedPayment = payModel.savePayment(payment);
            if(isSavedPayment){
                System.out.println("saved P");
                boolean  isUpdateDress = dressModel.updateDress(rent.getDressId());
                if(isUpdateDress){
                    System.out.println("saved d");
                    boolean savedRent = addRent(rent);
                    if(savedRent){
                        System.out.println("saved");
                        connection.commit();

                    }
                }
            }
            connection.rollback();
        }
        finally {
            connection.setAutoCommit(true);
        }

        return true;
    }






    private boolean addRent(RentDto rent) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            String sql = "INSERT INTO rent VALUES(?, ?, ?,?,?,?,?,?,?)";
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setString(1, rent.getRentId());
            pstm.setString(2, rent.getDressId());
            pstm.setString(3, rent.getCusId());
            pstm.setString(4,rent.getPayId());
            pstm.setDate(5,Date.valueOf(rent.getDate() ));
            pstm.setDate(6,Date.valueOf(rent.getReturnDate()));
            pstm.setInt(7,rent.getNoOfDays());
            pstm.setBoolean(8,rent.isReturn());
            pstm.setBoolean(9,rent.isReserve());
           //
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


    public boolean completeRent(String rentId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql="UPDATE rent SET isreturn=? WHERE rent_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setBoolean(1,true);
        pstm.setString(2,rentId);

        return pstm.executeUpdate()>0;


    }

    public boolean deleteRent(String rentId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="DELETE FROM rent WHERE rent_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,rentId);
        return pstm.executeUpdate()>0;
    }

    public void deleteAllLateReservations() throws SQLException {
        List<RentDto> allRentals = getAllRentals();
        List<RentDto> lateReservations=new ArrayList<>();
        for(RentDto rent:allRentals){
            if(rent.isReserve()==false&&LocalDate.now().isAfter(rent.getDate())){
             deleteRent(rent.getRentId());

            }
        }


    }

    public boolean completeReservation(String rentId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="UPDATE rent SET isreturn=? WHERE rent_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setBoolean(1,true);
        pstm.setString(2,rentId);
        return pstm.executeUpdate()>0;
    }
}

