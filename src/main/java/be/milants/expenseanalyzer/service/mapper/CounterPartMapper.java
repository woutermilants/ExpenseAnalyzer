package be.milants.expenseanalyzer.service.mapper;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import be.milants.expenseanalyzer.expense.rest.model.ExpenseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ExpenseMapper.class })
public interface CounterPartMapper {

    CounterPart dtoToDomain(CounterPartDto counterPartDto);

    @Mapping(target = "expenses", ignore = true)
    CounterPartDto domainToDTO(CounterPart counterPart);

}
