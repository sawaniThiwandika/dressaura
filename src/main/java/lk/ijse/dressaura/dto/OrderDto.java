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
private  String returnDate;
private LocalDate date;
private String orderId;
private String measurementId;
private String inseam;
private String shoulder;
private  String neck;
private String hips;
private String waist;
private String bust;



}
