package be.milants.expenseanalyzer.data;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Receiving {
    @Id
    private String counterPartAccount;
    private String receivingAmount;
    private String statement;
    private String date;
    private String currentBalance;
}
