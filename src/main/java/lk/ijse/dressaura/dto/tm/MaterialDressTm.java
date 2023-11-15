package lk.ijse.dressaura.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javafx.scene.control.Button;
import java.awt.*;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MaterialDressTm {
    private String material_id;
    private String material_name;
    private double unitPrice;
    private double amount;
    private double total;
    private Button remove;



}
