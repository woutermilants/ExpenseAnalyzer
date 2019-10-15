package be.milants.expenseanalyzer.service.mapper;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import be.milants.expenseanalyzer.expense.rest.model.ExpenseDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { CounterPartMapper.class })
public interface ExpenseMapper {

    Expense dtoToDomain(ExpenseDto expenseDto);

    @Mapping(source = "date", target = "date", dateFormat = "dd/MM/yyyy")
    @Mapping(target = "recurringCost", ignore = true)
    ExpenseDto domainToDTO(Expense expense);

    List<ExpenseDto> domainToDTO(List<Expense> expense);
}
