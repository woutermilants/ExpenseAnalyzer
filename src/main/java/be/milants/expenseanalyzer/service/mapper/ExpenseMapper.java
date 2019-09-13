package be.milants.expenseanalyzer.service.mapper;

import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.expense.rest.model.ExpenseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ExpenseMapper {

    public abstract Expense dtoToDomain(ExpenseDto expenseDto);

    public abstract ExpenseDto domainToDTO(Expense expense);
}