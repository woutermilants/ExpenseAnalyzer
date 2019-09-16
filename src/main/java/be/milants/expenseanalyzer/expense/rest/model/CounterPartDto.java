package be.milants.expenseanalyzer.expense.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CounterPartDto {
    private String accountNumber;
    private String name;
    private boolean recurringCounterPart;
    private boolean ownAccount;
}
