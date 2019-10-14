package be.milants.expenseanalyzer.expense.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecurringCostDto {
    private String counterPartAccountNumber;
    private String description;
    private List<Long> expenseIds;
    private String recurringOption;
}
