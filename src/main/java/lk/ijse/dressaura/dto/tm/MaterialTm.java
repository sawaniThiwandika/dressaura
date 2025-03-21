package lk.ijse.dressaura.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class MaterialTm {
    private int number;
    private String id;
    private String name;
    private double unitPrice;
    private double qtyOnHand;
    private String availability;
    private Button restock;
    private Button delete;




}
