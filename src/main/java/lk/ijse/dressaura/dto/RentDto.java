package lk.ijse.dressaura.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RentDto {

    private LocalDate date;
    private LocalDate returnDate;
    private int noOfDays;
    private String dressId;
    private String cusId;
    private String payId;
    private String rentId;
    private boolean isReturn;
    private boolean isReserve;

    public LocalDate getDate() {
        return  date;
    }

    public void setDate (LocalDate date) {
        this.date = date;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public int  getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int  noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getDressId() {
        return dressId;
    }

    public void setDressId(String dressId) {
        this.dressId = dressId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getRentId() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId;
    }

    public RentDto(String rentId,LocalDate date, LocalDate returnDate, int noOfDays, String dressId, String cusId, String payId,boolean isreturn ,boolean isReserve) {
        this.date = date;
        this.returnDate = returnDate;
        this.noOfDays = noOfDays;
        this.dressId = dressId;
        this.cusId = cusId;
        this.payId = payId;
        this.rentId = rentId;
        this.isReturn=isreturn;
        this.isReserve=isReserve;

    }
}
