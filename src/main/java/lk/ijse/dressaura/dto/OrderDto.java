package lk.ijse.dressaura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class OrderDto {
private String payId;
private  String cusId;
private LocalDate returnDate;
private LocalDate date;
private String orderId;
private double inseam;
private double shoulder;
private  double neck;
private double hips;
private double waist;
private double bust;
private String description;
private Boolean isCompleted;
private Boolean isHandOver;
private String design;

    public OrderDto(String labelOrderIdText, String waistText, String inseamText, String shoulderText, String neckText,
                    String hipsText, String bustText, String text, String design) {
        this.orderId=labelOrderIdText;
        this.design=design;
        this.bust= Double.parseDouble(bustText);
        this.description=text;
        this.hips= Double.parseDouble(hipsText);
        this.inseam= Double.parseDouble(inseamText);
        this.neck= Double.parseDouble(neckText);
        this.shoulder= Double.parseDouble(shoulderText);
        this.waist= Double.parseDouble(waistText);
    }

}
