package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Direction;
import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import be.milants.expenseanalyzer.repository.CounterPartRepository;
import be.milants.expenseanalyzer.service.mapper.CounterPartMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CounterPartService {

    private CounterPartRepository counterPartRepository;
    private CounterPartMapper counterPartMapper;
    private ExpenseService expenseService;

    public void create(String accountNumber, String name) {
        counterPartRepository.save(new CounterPart(accountNumber, name));
    }

    public Page<CounterPartDto> findAll(PageRequest pageRequest) {
        Page<CounterPartDto> counterPartDtos = convertPage(counterPartRepository.findAll(pageRequest));
        counterPartDtos.stream()
                .forEach(this::calculateTotalsForCounterPart);
        return counterPartDtos;
    }

    private void calculateTotalsForCounterPart(CounterPartDto counterPartDto) {
        counterPartDto.setTotalAmountInCentsSpent(expenseService.getTotalPerCounterPart(counterPartDto.getAccountNumber(), Direction.COST));
        counterPartDto.setTotalAmountInCentsReceived(expenseService.getTotalPerCounterPart(counterPartDto.getAccountNumber(), Direction.INCOME));
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

   /* public Double calculateTotalForCounterPart(Long id, Direction cost) {
        Optional<CounterPart> optionalCounterPart = counterPartRepository.findById(id);
        if (optionalCounterPart.isPresent()) {
            expenseService.getTotalForCounterPart(id, cost);
        }
        return 0.0d;
    }*/

    public CounterPartDto updateCounterpart(String accountNumber, CounterPartDto counterPartDto) {
        Optional<CounterPart> optionalCounterPart = counterPartRepository.findByAccountNumber(accountNumber);
        if (optionalCounterPart.isPresent()) {
            CounterPart counterPart = optionalCounterPart.get();
            counterPart.setOwnAccount(counterPartDto.isOwnAccount());
            counterPart.setRecurringCounterPart(counterPartDto.isRecurringCounterPart());
            return counterPartMapper.domainToDTO(counterPartRepository.save(counterPart));
        }
        return null;
    }

    public CounterPartDto findByAccountNumber(String accountNumber) {
        final Optional<CounterPart> optionalCounterPart = counterPartRepository.findByAccountNumber(accountNumber);
        if (optionalCounterPart.isPresent()) {
            return counterPartMapper.domainToDTO(optionalCounterPart.get());
        }
        return null;
    }
}
