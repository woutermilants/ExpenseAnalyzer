package be.milants.expenseanalyzer.expense.rest.model;

import lombok.Data;

@Data
public class ExpenseRecurringOnlyDto {

    private Long id;
    private boolean recurringExpense;
}
