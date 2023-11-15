package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.PaymentDto;
import lk.ijse.dressaura.dto.SupplierDetailsDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupplierDetailsModel {
    public boolean saveSupplyDetails(SupplierDetailsDto dtoSD, PaymentDto dto) throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();

        String sql="INSERT INTO supply_details VALUES (?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dtoSD.getSupId());
        pstm.setString(2,dtoSD.getMaterialId());
        pstm.setDouble(3,dtoSD.getAmount());
        pstm.setDouble(4,dtoSD.getSelling_price());
        pstm.setString(5, dto.getPayId());

        return pstm.executeUpdate()>0;


    }
}
