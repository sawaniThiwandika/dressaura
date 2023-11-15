package lk.ijse.dressaura.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SupplierTm {
    private String number;
    private String supId;
    private String name;
    private String contact;
    private String email;
    private javafx.scene.control.Button updateBtn;
    private javafx.scene.control.Button deleteBtn;



    public SupplierTm(int i, String supId, String name, String contact, String email, javafx.scene.control.Button btnU, javafx.scene.control.Button btnD) {
        this.supId = supId;
        this.name = name;
        this.email = email;
        this.contact=contact;
        this.deleteBtn=btnD;
        this.updateBtn=btnU;
        this.number= String.valueOf(i);
    }

}
