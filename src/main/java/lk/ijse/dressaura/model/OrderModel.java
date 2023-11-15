package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.OrderDto;
import lk.ijse.dressaura.dto.PaymentDto;
import lk.ijse.dressaura.dto.tm.MaterialDressTm;

import java.sql.*;
import java.util.List;

public class OrderModel {
    private static PaymentModel modelP=new PaymentModel();
    private  static  MaterialModel modelM=new MaterialModel();
    private  static  OrderDetailModel modelOD=new OrderDetailModel();
    public static boolean placeOrder(List<MaterialDressTm> materialList, PaymentDto payment, OrderDto order) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isPaymentSaved=modelP.savePayment(payment);
            if(isPaymentSaved) {
                System.out.println("payment saved");
                boolean isOrderSaved =saveOrder(order);
                if (isOrderSaved) {
                    System.out.println("order saved");
                    boolean isUpdated = modelM.updateMaterial(materialList);
                    if (isUpdated) {
                        System.out.println("update qty");
                        boolean isOrderDetailSaved = modelOD.saveOrderDetails(order.getOrderId(), materialList);
                        if (isOrderDetailSaved) {
                            System.out.println("saved");
                            connection.commit();
                        }
                    }
                }
            }
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
        return true;

    }

    private static boolean saveOrder(OrderDto order) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="INSERT INTO orders VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,order.getOrderId());
        pstm.setString(3,order.getPayId());
        pstm.setString(2,order.getCusId());
        pstm.setDate(4, Date.valueOf(order.getDate()));
        pstm.setDate(5, Date.valueOf(order.getReturnDate()));
        pstm.setString(6,order.getBust());
        pstm.setString(7,order.getWaist());
        pstm.setString(8,order.getHips());
        pstm.setString(9,order.getNeck());
        pstm.setString(10,order.getShoulder());
        pstm.setString(11, order.getInseam());
        pstm.setString(12,order.getDescription());

         return  pstm.executeUpdate()>0;



    }

    public String generateNextOrderId() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return splitOrderId(resultSet.getString(1));
        }
        return splitOrderId(null);
    }

    private String splitOrderId(String currentOrderId) {
        if(currentOrderId != null) {
            String[] split = currentOrderId.split("O0");

            int id = Integer.parseInt(split[1]);
            id++;
            return "O00" + id;
        } else {
            return "O001";
        }
    }
}
