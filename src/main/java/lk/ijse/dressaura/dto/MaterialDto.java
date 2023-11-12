package lk.ijse.dressaura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MaterialDto {
    private String materialId;
    private double qtyOnHand;
    private double unitPrice;
    private String name;

    @Override
    public String toString() {
        return "MaterialDto{" +
                "materialId='" + materialId + '\'' +
                ", qtyOnHand=" + qtyOnHand +
                ", unitPrice=" + unitPrice +
                ", name='" + name + '\'' +
                '}';
    }


}
