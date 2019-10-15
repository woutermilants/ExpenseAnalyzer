package be.milants.expenseanalyzer.expense.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CounterPartDto {
    private String accountNumber;
    private String name;
    private boolean recurringCounterPart;
    private boolean ownAccount;
    private BigDecimal totalAmountReceived;
    private BigDecimal totalAmountSpent;
    private List<ExpenseDto> expenses;
    private RecurringCostDto recurringCost;
}
