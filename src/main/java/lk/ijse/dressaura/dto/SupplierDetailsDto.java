package lk.ijse.dressaura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDetailsDto {
    private String supId;
    private String materialId;
    private int amount;
    private double selling_price;

}
