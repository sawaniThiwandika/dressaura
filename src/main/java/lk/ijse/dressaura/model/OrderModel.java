package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.OrderDto;
import lk.ijse.dressaura.dto.PaymentDto;
import lk.ijse.dressaura.dto.tm.MaterialDressTm;
import lk.ijse.dressaura.sendEmail.Mail;

import java.sql.*;
import java.util.ArrayList;
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
        String sql="INSERT INTO orders VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,order.getOrderId());
        pstm.setString(3,order.getPayId());
        pstm.setString(2,order.getCusId());
        pstm.setDate(4, Date.valueOf(order.getDate()));
        pstm.setDate(5, Date.valueOf(order.getReturnDate()));
        pstm.setDouble(6,order.getBust());
        pstm.setDouble(7,order.getWaist());
        pstm.setDouble(8,order.getHips());
        pstm.setDouble(9,order.getNeck());
        pstm.setDouble(10,order.getShoulder());
        pstm.setDouble(11, order.getInseam());
        pstm.setString(12,order.getDescription());
        pstm.setBoolean(13,order.getIsCompleted());
        pstm.setBoolean(14,order.getIsHandOver());
        pstm.setString(15,order.getDesign());
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

    public List<OrderDto> getAllIncompleteOrders() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="SELECT* FROM orders where isHandOver=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setBoolean(1,false);
        ResultSet resultSet = pstm.executeQuery();
        List <OrderDto> orders=new ArrayList<>();
        while (resultSet.next()){
            orders.add(
                    new OrderDto(
                            resultSet.getString("pay_id"),
                            resultSet.getString("cus_id"),
                            resultSet.getDate("return_date").toLocalDate(),
                            resultSet.getDate("reservation_date").toLocalDate(),
                            resultSet.getString("order_id"),
                            resultSet.getDouble("inseam"),
                            resultSet.getDouble("shoulder"),
                            resultSet.getDouble("neck"),
                            resultSet.getDouble("hips"),
                            resultSet.getDouble("waist"),
                            resultSet.getDouble("bust"),
                            resultSet.getString("description"),
                            resultSet.getBoolean("isCompleted"),
                            resultSet.getBoolean("isHandOver"),
                            resultSet.getString("design")
                    ));
        }
        return orders;
    }

    public void sendNotification(OrderDto dto) throws SQLException {
        CustomerModel customerModel=new CustomerModel();
        Mail mail=new Mail();
        String []to={customerModel.searchCustomer(dto.getCusId()).getEmail()};
        String subject="Your Clothing Order is Complete!\n";
        String body="Dear customer,\n" +
                "\n" +
                "We're thrilled to inform you that your recent clothing order with DressAura Collections has been " +
                "successfully processed and completed! We want to express our gratitude for choosing us.\n" +
                "\n" +
                "Here are the details of your order:\n" +
                "\n" +
                "Order Number:"+ dto.getOrderId() + "\n" +
                "Your package is now on its way and should reach you by "+dto.getDate()+". If you have any questions or" +
                " concerns about your order, feel free to reply to this email or contact our customer support.\n" +
                "\n" +
                "We sincerely hope you love your new additions to your wardrobe. Thank you for shopping with us!\n" +
                "\n" +
                "Best regards,\n" +
                "\n" +
                "[DressAura Collections]\n";

        mail.sendFromGMail(to, subject, body);
    }

    public boolean updateOrder(OrderDto dto) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            String sql = "UPDATE orders SET  isCompleted=? ,isHandOver=? WHERE order_id = ?";
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setBoolean(1, dto.getIsCompleted());
            pstm.setBoolean(2, dto.getIsHandOver());
            pstm.setString(3, dto.getOrderId());


            boolean isSaved = pstm.executeUpdate() > 0;
            System.out.println(isSaved);
            if (isSaved) {
                System.out.println(isSaved);
                connection.commit();
            }
            connection.rollback();

        } finally {
            connection.setAutoCommit(true);
        }
        return true;
    }

    public boolean deleteOrder(OrderDto dto) throws SQLException {
        OrderDetailModel orderDetailModel=new OrderDetailModel();
        Connection connection = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isDeleted = orderDetailModel.deletOrderDetails(dto);
            if(isDeleted){
                boolean isDeleteOrder = deleteOrder(dto.getOrderId());
                if (isDeleteOrder){
                    connection.commit();
                }
            }
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            connection.setAutoCommit(true);
        }
        return true;



    }

    private boolean deleteOrder(String orderId) throws SQLException {

            Connection connection = DbConnection.getInstance().getConnection();
            String sql="DELETE FROM orders WHERE order_id=?";
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, orderId);

            return pstm.executeUpdate()>0;
    }

    public boolean updateOrderMeasurements(OrderDto dto) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            String sql = "UPDATE orders SET  design=? ,bust=?,description=?,hips=?,inseam=?,neck=?,shoulder=?,waist=? WHERE order_id = ?";
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setString(1, dto.getDesign());
            pstm.setDouble(2, dto.getBust());
            pstm.setString(3, dto.getDescription());
            pstm.setDouble(4,dto.getHips());
            pstm.setDouble(5,dto.getInseam());
            pstm.setDouble(6,dto.getNeck());
            pstm.setDouble(7,dto.getShoulder());
            pstm.setDouble(8,dto.getWaist());
            pstm.setString(9,dto.getOrderId());



            boolean isSaved = pstm.executeUpdate() > 0;
            System.out.println(isSaved);
            if (isSaved) {
                System.out.println(isSaved);
                connection.commit();
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
