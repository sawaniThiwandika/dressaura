package lk.ijse.dressaura.model;

import javafx.scene.control.Button;
import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.PaymentDto;
import lk.ijse.dressaura.dto.tm.PaymentTm;


import java.sql.*;
import java.util.ArrayList;

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
        if(currentPaymentId != null) {
            String[] split = currentPaymentId.split("P0");

            int id = Integer.parseInt(split[1]);
            id++;
            return "P00" + id;
        } else {
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



    public ArrayList<PaymentTm> getIncomePaymentDetails() throws SQLException {



        Connection connection = DbConnection.getInstance().getConnection();
        ArrayList<PaymentTm> paymentList=new ArrayList<>();
        String sql=" select c.cus_id,c.name,r.rent_id,p.pay_date,p.amount,p.pay_id\n" +
                "    from payment p\n" +
                "    join rent r\n" +
                "    on r.pay_id=p.pay_id\n" +
                "    join customer c\n" +
                "    on c.cus_id=r.cus_id order by pay_id desc";
        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        int i=0;
       while (resultSet.next()){
           Button btn=new Button("update");
           i++;
           paymentList.add(new PaymentTm(i,
                   resultSet.getString("pay_id"),
                   resultSet.getString("cus_id"),
                   resultSet.getString("name"),
                   String.valueOf(resultSet.getDate("pay_date")),
                   "rent",resultSet.getDouble("amount"),
                   btn));

       }
        String sql1=" select c.cus_id,c.name,o.order_id,p.pay_date,p.amount,p.pay_id\n" +
                "    from payment p\n" +
                "    join orders o\n" +
                "    on o.pay_id=p.pay_id\n" +
                "    join customer c\n" +
                "    on c.cus_id=o.cus_id order by pay_id desc";
        PreparedStatement pstm1=connection.prepareStatement(sql1);
        ResultSet resultSet1 = pstm1.executeQuery();

        while (resultSet1.next()){
            Button btn=new Button("update");
            i++;
            paymentList.add(new PaymentTm(i,
                    resultSet1.getString("pay_id"),
                    resultSet1.getString("cus_id"),
                    resultSet1.getString("name"),
                    String.valueOf(resultSet1.getDate("pay_date")),
                    "order",resultSet1.getDouble("amount"),
                    btn));

        }




      return paymentList;
    }

    public ArrayList<PaymentTm> getCostPaymentDetails() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        ArrayList<PaymentTm> paymentList=new ArrayList<>();
        String sql="select s.sup_id,s.name,sd.material_id,p.pay_date,p.amount,p.pay_id\n" +
                "    from payment p\n" +
                "    join supply_details sd\n" +
                "    on sd.pay_id=p.pay_id\n" +
                "    join supplier s\n" +
                "    on s.sup_id=sd.sup_id order by pay_id desc;";
        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        int i=0;
        while (resultSet.next()){
            Button btn=new Button("update");
            i++;
            paymentList.add(new PaymentTm(btn,i,
                    resultSet.getString("pay_id"),
                    resultSet.getString("sup_id"),
                    resultSet.getString("name"),
                    String.valueOf(resultSet.getDate("pay_date")), resultSet.getString(
                    "material_id"),resultSet.getDouble("amount"))
                    );

        }
        return paymentList;

    }

    public Double getPaymentDetails(String payId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="SELECT amount FROM payment WHERE pay_id=?";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1,payId);
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()){
           return resultSet.getDouble("amount");

        }

     return 0.0;
    }
}
