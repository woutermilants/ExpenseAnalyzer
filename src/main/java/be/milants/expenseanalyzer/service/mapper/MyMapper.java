package be.milants.expenseanalyzer.service.mapper;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import be.milants.expenseanalyzer.expense.rest.model.ExpenseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MyMapper {

    public abstract Expense dtoToDomain(ExpenseDto expenseDto);

    public abstract CounterPart dtoToDomain(CounterPartDto counterPartDto);

    public abstract CounterPartDto domainToDTO(CounterPart counterPart);

    @Mapping(source = "date", target = "date", dateFormat = "dd/MM/yyyy")
    public abstract ExpenseDto domainToDTO(Expense expense);

    public abstract List<ExpenseDto> domainToDTO(List<Expense> expense);
}
