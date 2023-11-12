package lk.ijse.dressaura.dto;

import java.time.LocalDate;
import java.util.Date;

public class PaymentDto {
    private String payId;
    private LocalDate date;
    private double amount;

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentDto(String payId, LocalDate date, Double amount) {
        this.payId = payId;
        this.date = date;
        this.amount = amount;
    }
}
