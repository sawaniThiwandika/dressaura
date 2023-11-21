package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.*;
import lk.ijse.dressaura.dto.tm.MaterialDressTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialModel {
    public static String generateNextId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT material_id FROM material ORDER BY material_id DESC LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return splitMaterialId(resultSet.getString(1));
        }
        return splitMaterialId(null);
    }





    private static String splitMaterialId(String currenMaterialId) {
        if(currenMaterialId!=null){
            String [] split= currenMaterialId.split("M0");
            int id=Integer.parseInt(split[1]);
            id++;
            return "M00"+id;
        }
        else {
            return "M001";
        }

    }

    public List<SupplierDto> getSupplierTableValues() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="SELECT * from supplier";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        List<SupplierDto>supplierDtos=new ArrayList<>();
        while (resultSet.next()){
            supplierDtos.add (new SupplierDto(resultSet.getString("sup_id"),resultSet.getString("name"),resultSet.getString("contact"),resultSet.getString("email")));
        }

return supplierDtos;
    }

    public SupplierDto searchSupplier(String supId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql="SELECT * from supplier WHERE sup_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,supId);
        ResultSet resultSet = pstm.executeQuery();
        SupplierDto dto = null;
        while (resultSet.next()){
        dto= new SupplierDto(resultSet.getString("sup_id"),resultSet.getString("name"),resultSet.getString("contact"),resultSet.getString("email"));
        }
        return dto;

    }

    public boolean saveMaterial(MaterialDto dtoM, PaymentDto dtoP, SupplierDetailsDto dtoSD) throws SQLException {
        PaymentModel payModel=new PaymentModel();
        SupplierDetailsModel supDetailsModel=new SupplierDetailsModel();

        boolean check = checkPreviousMaterialId(dtoM.getMaterialId());
        if(check){
            boolean isadd = updateMateralStock(dtoM, dtoP, dtoSD);
            return isadd;
        }
        else {
            Connection connection = null;

            try {
                connection = DbConnection.getInstance().getConnection();
                connection.setAutoCommit(false);

                boolean ispaymentSaved = payModel.savePayment(dtoP);
                if (ispaymentSaved) {
                    boolean isAddedMaterial = addMatirial(dtoM);
                    if (isAddedMaterial) {
                        boolean isSupplyDetailSaved = supDetailsModel.saveSupplyDetails(dtoSD, dtoP);
                        if (isSupplyDetailSaved) {
                            connection.commit();
                        }
                    }
                }
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
            return true;
        }
    }

    private boolean updateMateralStock(MaterialDto dtoM, PaymentDto dtoP, SupplierDetailsDto dtoSD) throws SQLException {
        PaymentModel payModel=new PaymentModel();
        SupplierDetailsModel supDetailsModel=new SupplierDetailsModel();
        Connection connection = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean ispaymentSaved = payModel.savePayment(dtoP);
            if (ispaymentSaved) {
                boolean isAddedMaterial = updatePreviousMatirial(dtoM,dtoSD);
                if (isAddedMaterial) {
                    boolean isSupplyDetailSaved = supDetailsModel.saveSupplyDetails(dtoSD, dtoP);
                    if (isSupplyDetailSaved) {
                        connection.commit();
                    }
                }
            }
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
        return true;
    }

    private boolean updatePreviousMatirial(MaterialDto dtoM, SupplierDetailsDto dtoSD) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE material SET qty_on_hand = qty_on_hand + ? WHERE material_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setDouble(1,dtoSD.getAmount());
        pstm.setString(2, dtoM.getMaterialId());

        return pstm.executeUpdate() > 0;

    }

    private boolean checkPreviousMaterialId(String materialId) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        String sql="select material_id FROM material GROUP BY  material_id";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()){
            if(resultSet.getString(1).equals(materialId)){
                return true;

            }
        }
        return false;
    }

    private boolean addMatirial(MaterialDto dtoM) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();

        String sql="INSERT INTO material VALUES (?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dtoM.getMaterialId());
        pstm.setString(2,dtoM.getName());
        pstm.setDouble(3,dtoM.getQtyOnHand());
        pstm.setDouble(4,dtoM.getUnitPrice());

       return pstm.executeUpdate()>0;


    }

    public List<MaterialDto> loadAllMaterials() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM material";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<MaterialDto> materialList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            materialList.add(new MaterialDto(
                    resultSet.getString(1),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getString(2)
            ));
        }

        return materialList;

    }

    public MaterialDto searchMaterial(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM material WHERE material_id = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        MaterialDto dto = null;

        if(resultSet.next()) {
            dto = new MaterialDto(
                    resultSet.getString(1),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getString(2)
            );
        }
        return dto;
    }

    public boolean updateMaterial(List<MaterialDressTm> materialList) throws SQLException {
        for(MaterialDressTm tm : materialList) {
            System.out.println("Item: " + tm);
            if(!updateQty(tm.getMaterial_id(), tm.getAmount())) {
                return false;
            }
        }
        return true;

    }

    private boolean updateQty(String materialId, double amount) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE material SET qty_on_hand = qty_on_hand - ? WHERE material_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setDouble(1,amount);
        pstm.setString(2, materialId);

        return pstm.executeUpdate() > 0;
    }

    public String getName(String materialId) throws SQLException {
        String name=null;
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "select name from material where material_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,materialId);
        ResultSet resultSet = pstm.executeQuery();
       while (resultSet.next()){
          name = resultSet.getString("name");
       }

     return  name;
    }

    public List<MaterialDto> getMaterialTableValues() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT *FROM material ";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        ArrayList<MaterialDto> materialList=new ArrayList<>();

        while(resultSet.next()) {materialList.add(new MaterialDto(

                resultSet.getString(1),
                resultSet.getDouble(3),
                resultSet.getDouble(4),
                resultSet.getString(2)

        ));
        }
        return materialList;

    }
}
