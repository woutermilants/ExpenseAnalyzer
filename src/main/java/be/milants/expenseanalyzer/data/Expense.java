package be.milants.expenseanalyzer.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Expense {

    @Id
    @GeneratedValue
    private Long id;
    private String accountNumber;
    private String accountName;
    private String currency;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(length = 1024)
    private String description;
    private String currentBalance;
    private Integer amountInCents;
    private Direction direction;
    private String counterPartAccount;
    private String counterPartName;
    private String statement;
}
