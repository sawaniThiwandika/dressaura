package lk.ijse.dressaura.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter@NoArgsConstructor
public class EmployeeTm {
    private int number;
    private String empId;
    private  String name;
    private String address;
    private  String email;
    private  String contact;
    private Button update;
    private Button delete;
    private String jobRole;

}
