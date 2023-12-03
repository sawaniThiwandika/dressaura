package lk.ijse.dressaura.model;

import lk.ijse.dressaura.controller.LoginFormController;
import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.*;
import lk.ijse.dressaura.dto.tm.MaterialDressTm;
import lk.ijse.dressaura.dto.tm.MaterialSupplierTm;
import lk.ijse.dressaura.sendEmail.Mail;

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
            if(id<10)
            {return "M00"+id;}
            else{
                return "M0"+id;
            }
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

    public boolean saveMaterial(List<MaterialSupplierTm> materialDetails, PaymentDto dtoP, String supContact) throws SQLException {
        PaymentModel payModel=new PaymentModel();
        SupplierDetailsModel supDetailsModel=new SupplierDetailsModel();
        List<Integer> indexes = checkPreviousMaterialId(materialDetails);
        List<Integer> newIndexes =new ArrayList<>();
        if (indexes.size()!=0){
        newIndexes=checkNewIndexes(materialDetails,indexes);}
        else{
            for (int i=0;i<materialDetails.size();i++){
                newIndexes.add(i);
            }

        }
        System.out.println("indexes"+indexes);
        System.out.println("newIndexes"+newIndexes);
        boolean ispaymentSaved = payModel.savePayment(dtoP);

        if(indexes.size()!= 0){
            boolean isadd = updateMateralStock(materialDetails, dtoP, indexes,supContact);
            //return isadd;
        }
        if(newIndexes.size()!=0) {


            Connection connection = null;

            try {
                connection = DbConnection.getInstance().getConnection();
                connection.setAutoCommit(false);
                if (ispaymentSaved) {
                    boolean isAddedMaterial = addMatirial(materialDetails, newIndexes);
                    if (isAddedMaterial) {
                        boolean isSupplyDetailSaved = supDetailsModel.saveSupplyDetails(supContact, dtoP, newIndexes, materialDetails);
                        if (isSupplyDetailSaved) {
                            connection.commit();
                        }
                    }
                }
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }

        }
        return true;
    }

    private boolean addMatirial(List<MaterialSupplierTm> materialDetails, List<Integer> newIndexes) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        System.out.println(newIndexes);
        String sql="INSERT INTO material VALUES (?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        System.out.println(newIndexes);
        for (int i=0;i<newIndexes.size();i++) {

            pstm.setString(1, materialDetails.get(newIndexes.get(i)).getMaterial_id());
            pstm.setString(2,materialDetails.get(newIndexes.get(i)).getMaterial_name());
            pstm.setDouble(3, materialDetails.get(newIndexes.get(i)).getAmount());
            pstm.setDouble(4,materialDetails.get(newIndexes.get(i)).getUnitPrice());
        }
        return pstm.executeUpdate()>0;
    }

    private List<Integer> checkNewIndexes(List<MaterialSupplierTm> materialDetails, List<Integer> indexes) throws SQLException {

        List<Integer> newIndexes= new ArrayList<>();
        Connection connection=DbConnection.getInstance().getConnection();
        String sql="select material_id FROM material GROUP BY  material_id";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        int y=-1;
       L1: for (int i=0;i<materialDetails.size();i++){
           for (int j=0;j<indexes.size();j++)
                if(indexes.get(j)!=i) {
                    newIndexes.add(i);

                }
            }
        System.out.println(newIndexes);
           return newIndexes;
        }






    private boolean updateMateralStock(List<MaterialSupplierTm> materialDetails, PaymentDto dtoP, List<Integer> indexes, String supContact) throws SQLException {
        PaymentModel payModel=new PaymentModel();
        SupplierDetailsModel supDetailsModel=new SupplierDetailsModel();
        Connection connection = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);



                boolean isAddedMaterial = updatePreviousMatirial(materialDetails,indexes);
                if (isAddedMaterial) {
                    boolean isSupplyDetailSaved = supDetailsModel.saveSupplyDetails(supContact, dtoP,indexes,materialDetails);
                    if (isSupplyDetailSaved) {
                        connection.commit();
                    }
                }

            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
        return true;
    }

    private boolean updatePreviousMatirial(List<MaterialSupplierTm> materialDetails, List<Integer> indexes) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE material SET qty_on_hand = qty_on_hand + ? WHERE material_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        for (int i=0;i<indexes.size();i++) {

            pstm.setDouble(1, materialDetails.get(indexes.get(i)).getAmount());
            pstm.setString(2, materialDetails.get(indexes.get(i)).getMaterial_id());
        }
        return pstm.executeUpdate() > 0;

    }

    private List<Integer> checkPreviousMaterialId(List<MaterialSupplierTm> materialList) throws SQLException {
        List<Integer> indexes= new ArrayList<>();
        Connection connection=DbConnection.getInstance().getConnection();
        String sql="select material_id FROM material GROUP BY  material_id";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()){
            for (int i=0;i<materialList.size();i++){
            if(resultSet.getString(1).equals(materialList.get(i).getMaterial_id())) {
                indexes.add(i);
                break;

            }
            }
        }
        System.out.println(indexes);
        return indexes;
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



    public void notifyLowInventory(String materialId, String materialName) throws SQLException {
       UserModel model=new UserModel();
        String adminEmail = model.getAdminEmail();
        Mail mail=new Mail();
        String []to={adminEmail};
        String subject="Announcement";
        String body="Material id: "+materialId+"\n Material name: "+materialName+"\nLow Inventory now...";
        mail.sendFromGMail(to, subject, body);


    }

    public boolean deleteMaterial(MaterialDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM material WHERE material_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dto.getMaterialId());
        return pstm.executeUpdate()>0;
    }
}
