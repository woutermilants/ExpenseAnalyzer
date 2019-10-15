package be.milants.expenseanalyzer.expense.rest.model;

import be.milants.expenseanalyzer.data.Direction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDto {
    private Long id;
    private String accountNumber;
    private String accountName;
    private String currency;
    private Date date;
    private String description;
    private String currentBalance;
    private BigDecimal amount;
    private Direction direction;
    private CounterPartDto counterPart;
    private String statement;
    private RecurringCostDto recurringCost;
}
