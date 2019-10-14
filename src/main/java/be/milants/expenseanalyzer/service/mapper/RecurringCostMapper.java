package be.milants.expenseanalyzer.service.mapper;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.data.RecurringCost;
import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import be.milants.expenseanalyzer.expense.rest.model.ExpenseDto;
import be.milants.expenseanalyzer.expense.rest.model.RecurringCostDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecurringCostMapper {

    RecurringCost dtoToDomain(RecurringCostDto recurringCostDto);

    RecurringCostDto domainToDTO(RecurringCost recurringCost);

    List<RecurringCostDto> domainToDTO(List<RecurringCost> recurringCosts);

    @Mapping(source = "date", target = "date", dateFormat = "dd/MM/yyyy")
    ExpenseDto domainToDTO(Expense expense);
}
