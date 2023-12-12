package lk.ijse.dressaura.dto.tm;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RentTm {
    private String rentId;
    private String cusId;
    private String name;
    private String dressId;
    private String contact;
    private String number;
    private LocalDate reserveDate;
    private LocalDate returnDate;
    private Button delete_button;
    private Button isreturn;
    private CheckBox reservation;



}
