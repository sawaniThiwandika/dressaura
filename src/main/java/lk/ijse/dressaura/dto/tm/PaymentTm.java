package lk.ijse.dressaura.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTm {
    private int number_income;
    private int number_cost;
    private String payIdIncome;
    private String payIdCost;
    private String cusId;
    private String supId;
    private String cusName;
    private String supName;
    private String dateIncome;
    private String dateCost;
    private String type;
    private String materialId;
    private Double amountCost;
    private Double amountIncome;
    private Button updatePC_button;
    private Button updatePI_button;

public PaymentTm(int number_income,String payIdIncome,String cusId,String cusName,String dateIncome,String type,Double amountIncome,Button updatePI_button){
   this.number_income=number_income;
   this.payIdIncome=payIdIncome;
   this.cusId=cusId;
   this.cusName=cusName;
   this.dateIncome=dateIncome;
   this.type=type;
   this.amountIncome=amountIncome;
   this.updatePI_button=updatePI_button;
}
    public PaymentTm(Button updatePC_button,int number_cost,String payIdCost,String supId,String supName,String dateCost,Double amountCost){
        this.number_cost=number_cost;
        this.payIdCost=payIdCost;
        this.supId=supId;
        this.supName=supName;
        this.dateCost=dateCost;
       // this.materialId=materialId;
        this.amountCost=amountCost;
        this.updatePC_button=updatePC_button;
    }


}
