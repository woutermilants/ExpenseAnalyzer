package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import be.milants.expenseanalyzer.repository.CounterPartRepository;
import be.milants.expenseanalyzer.service.mapper.CounterPartMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CounterPartService {

    private CounterPartRepository counterPartRepository;
    private CounterPartMapper counterPartMapper;

    public void create(String accountNumber, String name) {
        counterPartRepository.save(new CounterPart(accountNumber, name));
    }

    public Page<CounterPartDto> findAll(PageRequest pageRequest) {
        return convertPage(counterPartRepository.findAll(pageRequest));
    }

    private Page<CounterPartDto> convertPage(Page<CounterPart> page) {
        List<CounterPart> cameras = page.getContent();
        return new PageImpl<>(
                cameras.stream()
                        .map(counterPartMapper::domainToDTO)
                        .collect(Collectors.toList()),
                page.getPageable(),
                page.getTotalElements()
        );
    }

}
