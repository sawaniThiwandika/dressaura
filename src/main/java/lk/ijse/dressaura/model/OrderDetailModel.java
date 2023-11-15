package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.tm.MaterialDressTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailModel {
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
}
