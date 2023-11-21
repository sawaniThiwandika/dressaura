package lk.ijse.dressaura.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class ViewMaterialsTm {
    private String material_id;
    private String name;
    private Double amount;

}
