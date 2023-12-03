package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.OrderDetailsDto;
import lk.ijse.dressaura.dto.OrderDto;
import lk.ijse.dressaura.dto.tm.MaterialDressTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailModel {
    public static List<OrderDetailsDto> getAllDetails() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        List<OrderDetailsDto> detailsList=new ArrayList<>();
        String sql = "SELECT * FROM order_details";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()){
           detailsList.add(new OrderDetailsDto(
                   resultSet.getString("order_id"),
                   resultSet.getDouble("material_amount"),
                   resultSet.getString("material_id")));
        }
        return detailsList;
    }

    public boolean saveOrderDetails(String orderId, List<MaterialDressTm> materialList) throws SQLException {
        for(MaterialDressTm tm : materialList) {
            if(!saveOrderDetails(tm,orderId)) {
                return false;
            }
        }
        return true;
    }
    private boolean saveOrderDetails(MaterialDressTm tm,String orderId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO order_details VALUES(?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, orderId);
        pstm.setString(2, tm.getMaterial_id());
        pstm.setDouble(3, tm.getAmount());


        return pstm.executeUpdate() > 0;
    }

    public ArrayList<OrderDetailsDto> getOrderDetails(String orderId) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        ArrayList<OrderDetailsDto> detailsList=new ArrayList<>();
        String sql = "select* FROM order_details where order_id=? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,orderId);
        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            detailsList.add(new OrderDetailsDto(
                    resultSet.getString("order_id"),
                    resultSet.getDouble("material_amount"),
                    resultSet.getString("material_id")

            ));

        }
        return detailsList;

    }

    public boolean deletOrderDetails(OrderDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM order_details WHERE order_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getOrderId());

        return pstm.executeUpdate() > 0;
    }
}
