package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.PaymentDto;

import java.sql.*;

public class PaymentModel {
    public String generateNextId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT pay_id FROM payment ORDER BY pay_id DESC LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return splitPaymentId(resultSet.getString(1));
        }
        return splitPaymentId(null);
    }

    private String splitPaymentId(String currentPaymentId) {
        if(currentPaymentId!=null){
            String [] split= currentPaymentId.split("P0");
            int id=Integer.parseInt(split[1]);
            id++;
            return "P00"+id;
        }
        else {
            return "P001";
        }

    }

    public boolean savePayment(PaymentDto payment) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="INSERT INTO payment Values (?,?,?) ";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1,payment.getPayId());
        pstm.setDouble(2,payment.getAmount());
        pstm.setDate(3, Date.valueOf(payment.getDate()));

       return(pstm.executeUpdate()>0);
    }
}
