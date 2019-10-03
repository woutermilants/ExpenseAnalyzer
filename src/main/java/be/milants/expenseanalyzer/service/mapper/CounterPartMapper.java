package be.milants.expenseanalyzer.service.mapper;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class CounterPartMapper {

    public abstract CounterPart dtoToDomain(CounterPartDto counterPartDto);

    public abstract CounterPartDto domainToDTO(CounterPart counterPart);
}
