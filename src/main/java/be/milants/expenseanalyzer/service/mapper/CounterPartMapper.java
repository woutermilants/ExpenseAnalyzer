package be.milants.expenseanalyzer.service.mapper;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import be.milants.expenseanalyzer.expense.rest.model.ExpenseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CounterPartMapper {

    Expense dtoToDomain(ExpenseDto expenseDto);

    CounterPart dtoToDomain(CounterPartDto counterPartDto);

    CounterPartDto domainToDTO(CounterPart counterPart);

    @Mapping(source = "date", target = "date", dateFormat = "dd/MM/yyyy")
    @Mapping(target = "counterPart", ignore = true)
    ExpenseDto domainToDTO(Expense expense);

    List<ExpenseDto> domainToDTO(List<Expense> expense);
}
