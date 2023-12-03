package lk.ijse.dressaura.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class MaterialSupplierTm {
    private String material_id;
    private String material_name;
    private double unitPrice;
    private double amount;
    private double total;
    private Button remove;

}
