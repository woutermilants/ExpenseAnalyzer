package be.milants.expenseanalyzer.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cost {
    @Id
    @Column(length = 40)
    private String counterPartAccount;
    private String costAmount;
    private String statement;
    private String date;
    private String currentBalance;
}
