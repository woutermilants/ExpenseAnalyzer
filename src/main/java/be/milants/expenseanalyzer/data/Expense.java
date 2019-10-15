package be.milants.expenseanalyzer.data;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "expense")
@ToString
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 40)
    private Long id;
    private String accountNumber;
    private String accountName;
    private String currency;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(length = 1024)
    private String description;
    private String currentBalance;
    private BigDecimal amount;
    private Direction direction;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="counterPart_accountNumber", nullable=false)
    private CounterPart counterPart;
    private String statement;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="recurringCost_id", nullable=true)
    private RecurringCost recurringCost;
}
