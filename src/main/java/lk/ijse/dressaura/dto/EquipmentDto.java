package lk.ijse.dressaura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentDto {
    private String equipmentId;
    private String userName;
    private String name;
    private double price;
    private LocalDate date;
    private  String description;

}
