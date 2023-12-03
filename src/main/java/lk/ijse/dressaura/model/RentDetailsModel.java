package lk.ijse.dressaura.model;

import lk.ijse.dressaura.db.DbConnection;
import lk.ijse.dressaura.dto.DressDto;
import lk.ijse.dressaura.dto.PaymentDto;
import lk.ijse.dressaura.dto.RentDetailsDto;
import lk.ijse.dressaura.sendEmail.Mail;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentDetailsModel {
    public List<RentDetailsDto> getAllRentals() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT* FROM rent_details where isReturn=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setBoolean(1, false);
        ResultSet resultSet = pstm.executeQuery();
        List<RentDetailsDto> rentalDetails = new ArrayList<>();
        while (resultSet.next()) {
            rentalDetails.add(
                    new RentDetailsDto(
                            resultSet.getString("dress_id"),
                            resultSet.getString("rent_id"),
                            resultSet.getDate("reservation_date").toLocalDate(),
                            resultSet.getDate("return_date").toLocalDate(),
                            resultSet.getBoolean("isReturn"),
                            resultSet.getBoolean("isReserve")
                    ));
        }
        return rentalDetails;
    }

    public boolean saveRentDetails(List<RentDetailsDto> rentDetails) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        for (int i = 0; i < rentDetails.size(); i++) {
            String sql = "INSERT INTO rent_details VALUES (?,?,?,?,?,?)";
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, rentDetails.get(i).getDress_id());
            pstm.setString(2, rentDetails.get(i).getRent_id());
            pstm.setDate(3, Date.valueOf(rentDetails.get(i).getReservation_date()));
            pstm.setDate(4, Date.valueOf(rentDetails.get(i).getReturn_date()));
            pstm.setBoolean(5, rentDetails.get(i).isReturn());
            pstm.setBoolean(6, rentDetails.get(i).isReserve());
            pstm.executeUpdate();

        }
        return true;
    }


    public boolean completeReservation(String rentId, String dressId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE rent_details SET isReserve=? WHERE rent_id=? and dress_id=?";
        RentModel rentModel = new RentModel();

        boolean isComplete = checkPaymentComplete(rentId);
        if (isComplete) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setBoolean(1, true);
            pstm.setString(2, rentId);
            pstm.setString(3, dressId);
            boolean b = pstm.executeUpdate() > 0;
            System.out.println("complete reservation" + b);
            return b;
        } else {
            DressModel dressModel = new DressModel();
            PaymentModel paymentModel = new PaymentModel();
            String payId = paymentModel.generateNextId();
            PaymentDto dto = new PaymentDto(payId, LocalDate.now(), paymentModel.getPaymentDetails(rentModel.searchDetails
                    (rentId).getPayId()));
            boolean isPaid = paymentModel.savePayment(dto);
            if (isPaid) {
                boolean paymentComplete = rentModel.completePayment(rentId);
                if (paymentComplete) {
                    String sql1 = "UPDATE rent_details SET isReserve=? WHERE rent_id=? and dress_id=?";
                    PreparedStatement pstm1 = connection.prepareStatement(sql1);
                    pstm1.setBoolean(1, true);
                    pstm1.setString(2, rentId);
                    pstm1.setString(3, dressId);
                    boolean b = pstm1.executeUpdate() > 0;
                    System.out.println("complete reservation" + b);
                    return b;
                }
            }
        }
        return true;

    }

    public boolean completeRent(String rentId, String dressId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "UPDATE rent_details SET isReturn=? WHERE rent_id=? and dress_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setBoolean(1, true);
        pstm.setString(2, rentId);
        pstm.setString(3, dressId);
        return pstm.executeUpdate() > 0;
    }

    public RentDetailsDto searchDetails(String rentId) throws SQLException {
        System.out.println(rentId + "abc");
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "select* from rent_details where rent_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, rentId);
        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            RentDetailsDto dto = new RentDetailsDto(
                    resultSet.getString("dress_id"),
                    resultSet.getString("rent_id"),
                    resultSet.getDate("reservation_date").toLocalDate(),
                    resultSet.getDate("return_date").toLocalDate(),
                    resultSet.getBoolean("isReturn"),
                    resultSet.getBoolean("isReserve")
            );
            return dto;
        }
        return null;
    }

    public boolean deleteRent(String rentId, String dressId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM rent_details WHERE (rent_id=?and dress_id=?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, rentId);
        pstm.setString(2, dressId);
        return pstm.executeUpdate() > 0;
    }


    public boolean deleteRentals(DressDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "DELETE FROM rent_details WHERE dress_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getDressId());
        return pstm.executeUpdate() > 0;
    }

    public List<RentDetailsDto> getEveryRentals() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT* FROM rent_details ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        List<RentDetailsDto> rentalDetails = new ArrayList<>();
        while (resultSet.next()) {
            rentalDetails.add(
                    new RentDetailsDto(
                            resultSet.getString("dress_id"),
                            resultSet.getString("rent_id"),
                            resultSet.getDate("reservation_date").toLocalDate(),
                            resultSet.getDate("return_date").toLocalDate(),
                            resultSet.getBoolean("isReturn"),
                            resultSet.getBoolean("isReserve")
                    ));
        }
        return rentalDetails;
    }

    public boolean checkPaymentComplete(String rentId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT payment_complete FROM rent WHERE rent_id=? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, rentId);
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            return resultSet.getBoolean("payment_complete");
        }


        return false;
    }

    public void checkLateReturn() throws SQLException {
        LocalDate currentDate = LocalDate.now();
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM rent_details WHERE isReturn=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setBoolean(1,false);
        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {

            if (resultSet.getDate("return_date").toLocalDate().isBefore(LocalDate.now())) {

                RentModel rentModel=new RentModel();
                CustomerModel customerModel=new CustomerModel();
                System.out.println(  customerModel.searchCustomer(rentModel.searchDetails(resultSet.getString("rent_id")).getCusId()).getEmail()    );
                String email = customerModel.searchCustomer(rentModel.searchDetails(resultSet.getString("rent_id")).getCusId()).getEmail();
                String name= customerModel.searchCustomer(rentModel.searchDetails(resultSet.getString("rent_id")).getCusId()).getName();
                Mail mail = new Mail();
                String[] to = {email};
                String subject = "Overdue Return Notice for Your Rented Costume\n";
                String body= "Dear "+name+ "\n" +
                        "\n" +
                        "We hope this email finds you well. We wanted to bring to your attention that the return date for the costume you rented from [Your Clothing Shop] has passed.\n" +
                        "\n" +
                        "As of [Current Date], your costume is overdue. To avoid any additional late fees, we kindly request that you return the costume at your earliest convenience. If there are any issues or concerns regarding the return, please reach out to us promptly at [Your Contact Information].\n" +
                        "\n" +
                        "We appreciate your immediate attention to this matter and look forward to the timely return of the costume. Thank you for choosing [Your Clothing Shop].\n" +
                        "\n" +
                        "Best regards,\n";
                mail.sendFromGMail(to, subject, body);
            }
            //else if (  (resultSet.getDate("return_date")).equals(LocalDate.now().plusDays(1))) {
                else if ((( resultSet.getDate("return_date")).toLocalDate().isEqual(currentDate.plusDays(1)))&& (resultSet.getBoolean("isReserve")) ) {
                RentModel rentModel = new RentModel();
                CustomerModel customerModel = new CustomerModel();
                System.out.println(  customerModel.searchCustomer(rentModel.searchDetails(resultSet.getString("rent_id")).getCusId()).getEmail()    );
               String name= customerModel.searchCustomer(rentModel.searchDetails(resultSet.getString("rent_id")).getCusId()).getName();
                    String email = customerModel.searchCustomer(rentModel.searchDetails(resultSet.getString("rent_id")).getCusId()).getEmail();
                    Mail mail = new Mail();
                    String[] to = {email};
                    String subject = "Return Date for Your Costume Rental Tomorrow\n";
                    String body = "Dear "+name+ "\n" +
                            "\n" +
                            "We hope this email finds you well. We wanted to remind you that the return date for the costume you rented from us is tomorrow.\n" +
                            "\n" +
                            "Please ensure that the costume is returned by [Return Date]. If you have any questions or need to discuss an extension, feel free to reach out to us at 0763519008.\n" +
                            "\n" +
                            "We appreciate your prompt attention to this matter and hope you enjoyed your time with our costume. Thank you for choosing DressAura!\n" +
                            "\n" +
                            "Best regards\n DressAura,\n";
                    mail.sendFromGMail(to, subject, body);
                }

            }
        }



}

