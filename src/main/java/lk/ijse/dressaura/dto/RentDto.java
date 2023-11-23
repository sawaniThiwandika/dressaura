package lk.ijse.dressaura.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RentDto {
    private String rentId;
    private String cusId;
    private String payId;
    private LocalDate date;
    private boolean paymentType;


}
