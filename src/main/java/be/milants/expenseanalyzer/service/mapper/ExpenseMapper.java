package be.milants.expenseanalyzer.service.mapper;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import be.milants.expenseanalyzer.expense.rest.model.ExpenseDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    Expense dtoToDomain(ExpenseDto expenseDto);

    CounterPart dtoToDomain(CounterPartDto counterPartDto);

    @Mapping(target = "expenses", ignore = true)
    CounterPartDto domainToDTO(CounterPart counterPart);

    @Mapping(source = "date", target = "date", dateFormat = "dd/MM/yyyy")
    ExpenseDto domainToDTO(Expense expense);

    List<ExpenseDto> domainToDTO(List<Expense> expense);
}
