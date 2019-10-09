package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.repository.CounterPartRepository;
import be.milants.expenseanalyzer.service.mapper.ExpenseMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CounterPartService {

    private CounterPartRepository counterPartRepository;
    private ExpenseMapper expenseMapper;
    private ExpenseService expenseService;

    public Page<CounterPart> findAll(PageRequest pageRequest) {
        return counterPartRepository.findAll(pageRequest);
    }


   /* public Double calculateTotalForCounterPart(Long id, Direction cost) {
        Optional<CounterPart> optionalCounterPart = counterPartRepository.findById(id);
        if (optionalCounterPart.isPresent()) {
            expenseService.getTotalForCounterPart(id, cost);
        }
        return 0.0d;
    }*/

    public CounterPart updateCounterpart(String accountNumber, CounterPart counterPart) {
        final Optional<CounterPart> optionalCounterPart = counterPartRepository.findByAccountNumber(accountNumber);
        if (optionalCounterPart.isPresent()) {
            CounterPart persistedCounterPart = optionalCounterPart.get();
            persistedCounterPart.setOwnAccount(counterPart.isOwnAccount());
            persistedCounterPart.setRecurringCounterPart(counterPart.isRecurringCounterPart());
            return counterPartRepository.save(persistedCounterPart);
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
