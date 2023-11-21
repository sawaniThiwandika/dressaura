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



}
