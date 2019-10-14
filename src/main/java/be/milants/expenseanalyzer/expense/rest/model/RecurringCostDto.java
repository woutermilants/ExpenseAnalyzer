package be.milants.expenseanalyzer.expense.rest.model;

import be.milants.expenseanalyzer.data.RecurringOption;
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
public class RecurringCostDto {
    private CounterPartDto counterPart;
    private String description;
    private List<ExpenseDto> expenses;
    private RecurringOption recurringOption;
}
