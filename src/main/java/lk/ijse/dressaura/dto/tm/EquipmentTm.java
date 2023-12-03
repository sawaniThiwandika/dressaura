package lk.ijse.dressaura.dto.tm;


import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class EquipmentTm {
   private int number;
    private String id;
    private String name;
    private  double price;
    private LocalDate date;
    private Button update;
    private Button delete;
    private String description;



}
