package lk.ijse.dressaura.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RentDressCartTm {
    private String rentId;
    private String dressId;
    private String name;
    private LocalDate rentDate;
    private LocalDate returnDate;
    private double price;
    Button remove;
    Button update;

}
