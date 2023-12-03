package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.PaymentDto;
import lk.ijse.dressaura.dto.SupplierDetailsDto;
import lk.ijse.dressaura.dto.tm.MaterialSupplierTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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

    public boolean saveSupplyDetails(String supContact, PaymentDto dtoP, List<Integer> indexes,
                                     List<MaterialSupplierTm> materialDetails) throws SQLException {
        SupplierModel model=new SupplierModel();
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="INSERT INTO supply_details VALUES (?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        for (int i=0;i<indexes.size();i++){
            pstm.setString(1,model.searchSupplier(supContact).getSupId());
            pstm.setString(2,materialDetails.get(indexes.get(i)).getMaterial_id());
            pstm.setDouble(3,materialDetails.get(indexes.get(i)).getAmount());
            pstm.setDouble(4,materialDetails.get(indexes.get(i)).getUnitPrice());
            pstm.setString(5, dtoP.getPayId());

        }
        return pstm.executeUpdate()>0;
    }
}
