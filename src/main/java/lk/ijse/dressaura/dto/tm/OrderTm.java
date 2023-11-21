package lk.ijse.dressaura.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderTm {
    private int number;
    private String orderId;
    private String customerId;
    private String customerName;
    private LocalDate date;
    private LocalDate returnDate;
    private Button view;
    private Button finish;
    private Button handOver;
    private Button delete;


}
