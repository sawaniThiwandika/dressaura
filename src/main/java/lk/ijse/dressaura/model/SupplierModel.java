package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.SupplierDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {
    boolean isDuplicate=false;
    public String generateNextSupplierId() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT sup_id FROM supplier ORDER BY sup_id DESC LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return splitSupplierId(resultSet.getString(1));
        }
        return splitSupplierId(null);
    }
    private String splitSupplierId(String currentSupplierId) {
        if(currentSupplierId != null) {
            String[] split = currentSupplierId.split("S0");
            int id = Integer.parseInt(split[1]); //01
            id++;
            return "S00" + id;
        } else {
            return "S001";
        }
    }
  public boolean checkDuplicates(SupplierDto dto) throws SQLException {
      List<SupplierDto> supplierTableValues = getSuplierTableValues();
      for (int i = 0; i < supplierTableValues.size(); i++) {
          if (dto.getSupId().equals(supplierTableValues.get(i).getSupId())) {
              //updateEmployee();
              return true;
          }


      }
      return false;
  }

    public boolean saveSupplier(SupplierDto dto) throws SQLException {
        isDuplicate=checkDuplicates(dto);

        if (!isDuplicate) {
            Connection connection = null;
            try {
                connection = DbConnection.getInstance().getConnection();
                connection.setAutoCommit(false);
                String sql = "INSERT INTO supplier VALUES(?, ?, ?, ?)";
                PreparedStatement pstm = connection.prepareStatement(sql);

                pstm.setString(1, dto.getSupId());
                pstm.setString(2, dto.getName());
                pstm.setString(3, dto.getContact());
                pstm.setString(4, dto.getEmail());


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

        else {
            Connection connection = null;
            try {
                connection = DbConnection.getInstance().getConnection();
                connection.setAutoCommit(false);
                String sql = "UPDATE supplier SET  name = ?,contact=? ,email=?WHERE sup_id = ?";
                PreparedStatement pstm = connection.prepareStatement(sql);


                pstm.setString(1, dto.getName());
                pstm.setString(2, dto.getContact());
                pstm.setString(3, dto.getEmail());
                pstm.setString(4, dto.getSupId());


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
        }



    public List<SupplierDto> getSuplierTableValues() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT *FROM supplier ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        ArrayList<SupplierDto> supplierrList=new ArrayList<>();

        while(resultSet.next()) {supplierrList.add(new SupplierDto(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4)));
        }
        return supplierrList;
    }


    public SupplierDto searchSupplier(String contact) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        String sql= "SELECT * FROM supplier WHERE contact=?";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1, contact);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            SupplierDto supplier=new SupplierDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4))
                 ;
            return supplier;
        }
        else{
            return  null;
        }
    }

    public boolean deleteSupplier(String supId) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        String sql= "DELETE FROM supplier WHERE sup_id=?";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1,supId);
       return pstm.executeUpdate()>0;
    }
}
