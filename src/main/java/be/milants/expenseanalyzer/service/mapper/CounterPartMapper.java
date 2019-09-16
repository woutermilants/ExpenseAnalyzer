package be.milants.expenseanalyzer.service.mapper;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CounterPartMapper {

    public abstract CounterPart dtoToDomain(CounterPartDto counterPartDto);

    public abstract CounterPartDto domainToDTO(CounterPart counterPart);
}