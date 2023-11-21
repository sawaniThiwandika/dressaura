package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.PaymentDto;
import lk.ijse.dressaura.dto.RentDetailsDto;
import lk.ijse.dressaura.dto.RentDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AddRentModel {
    public boolean saveRent(RentDto rents, List<RentDetailsDto> rentDetails, PaymentDto payment) throws SQLException {
        PaymentModel payModel=new PaymentModel();
        DressModel dressModel=new DressModel();
        RentModel rentModel=new RentModel();
        RentDetailsModel  rentDetailsModel=new RentDetailsModel();
        Connection connection = null;

        try {
            System.out.println("go");
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean  isSavedPayment = payModel.savePayment(payment);
            if(isSavedPayment){
                System.out.println("saved P");
                boolean  isUpdateDress = dressModel.updateDress(rentDetails);
                if(isUpdateDress){
                    System.out.println("saved d");
                    boolean savedRent = rentModel.addRent(rents);
                    System.out.println(savedRent);
                    if(savedRent){

                        System.out.println("saved rent");
                      boolean saveDetails=rentDetailsModel.saveRentDetails(rentDetails);
                      if(saveDetails)
                      {
                          System.out.println("save rent details");
                          connection.commit();
                      }

                    }
                }
            }
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.setAutoCommit(true);
        }

        return true;
    }
}
