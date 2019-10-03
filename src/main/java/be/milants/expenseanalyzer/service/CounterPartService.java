package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Direction;
import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import be.milants.expenseanalyzer.repository.CounterPartRepository;
import be.milants.expenseanalyzer.service.mapper.MyMapper;
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
    private MyMapper myMapper;
    private ExpenseService expenseService;

    public Page<CounterPartDto> findAll(PageRequest pageRequest) {
        Page<CounterPartDto> counterPartDtos = convertPage(counterPartRepository.findAll(pageRequest));
        counterPartDtos.stream()
                .forEach(this::calculateTotalsForCounterPart);
        return counterPartDtos;
    }

    private void calculateTotalsForCounterPart(CounterPartDto counterPartDto) {
        counterPartDto.setTotalAmountSpent(expenseService.getTotalPerCounterPart(counterPartDto.getAccountNumber(), Direction.COST));
        counterPartDto.setTotalAmountReceived(expenseService.getTotalPerCounterPart(counterPartDto.getAccountNumber(), Direction.INCOME));
    }

    private Page<CounterPartDto> convertPage(Page<CounterPart> page) {
        List<CounterPart> cameras = page.getContent();
        return new PageImpl<>(
                cameras.stream()
                        .map(myMapper::domainToDTO)
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
            return myMapper.domainToDTO(counterPartRepository.save(counterPart));
        }
        return null;
    }

    public Optional<CounterPart> findByAccountNumber(String accountNumber) {
        return counterPartRepository.findByAccountNumber(accountNumber);
    }

    public CounterPart save(CounterPart counterPart) {
        return counterPartRepository.save(counterPart);
    }
}
