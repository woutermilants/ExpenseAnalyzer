package be.milants.expenseanalyzer.data;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Income {
    @Id
    @Column(length = 40)
    private String counterPartAccount;
    private String incomeAmount;
    private String statement;
    private String date;
    private String currentBalance;
}
