package lk.ijse.dressaura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RentDetailsDto {
    private String dress_id;
    private String rent_id;
    private LocalDate reservation_date;
    private LocalDate return_date;
    private boolean isReturn;
    private boolean isReserve;

}
