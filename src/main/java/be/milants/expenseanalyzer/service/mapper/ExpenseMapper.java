package be.milants.expenseanalyzer.service.mapper;

import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.expense.rest.model.ExpenseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ExpenseMapper {

    public abstract Expense dtoToDomain(ExpenseDto expenseDto);

    @Mappings({
            @Mapping(target="counterPartAccount", source="expense.counterPart.accountNumber"),
            @Mapping(target="counterPartName", source="expense.counterPart.name")
    })
    public abstract ExpenseDto domainToDTO(Expense expense);

    public abstract List<ExpenseDto> domainToDTO(List<Expense> expense);
}
