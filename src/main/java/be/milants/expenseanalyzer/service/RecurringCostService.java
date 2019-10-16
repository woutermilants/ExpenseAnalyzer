package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.data.RecurringCost;
import be.milants.expenseanalyzer.data.RecurringOption;
import be.milants.expenseanalyzer.repository.RecurringCostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecurringCostService {

    private final RecurringCostRepository recurringCostRepository;
    private final CounterPartService counterPartService;
    private final ExpenseService expenseService;

    public RecurringCost createRecurringCost(String counterPartAccountNumber, String description, List<Long> expensesIds, String recurringOption) {
        final CounterPart counterPart = counterPartService.findByAccountNumber(counterPartAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Counter part account number not known"));
        List<Expense> expenses = expensesIds.stream()
                .map(expenseService::getExpenseById)
                .collect(toList());
        final RecurringCost recurringCost = RecurringCost.builder()
                .counterPart(counterPart)
                .description(description)
                .expenses(expenses)
                .recurringOption(RecurringOption.valueOf(recurringOption))
                .build();
        recurringCostRepository.save(recurringCost);
        for (Expense expense : expenses) {
            expense.setRecurringCost(recurringCost);
            expenseService.save(expense);
        }
        return recurringCost;
    }

    public List<RecurringCost> findAll() {
        return recurringCostRepository.findAll();
    }

    public RecurringCost createOrAddCost(CounterPart counterPart, RecurringOption recurringOption, Expense expense) {

        final RecurringCost recurringCost = recurringCostRepository.
                findByCounterPartAndRecurringOption(counterPart, recurringOption)
                .orElse(createRecurringCost(counterPart, recurringOption));

        recurringCost.getExpenses().add(expense);
        final RecurringCost persistedRecurringCost = recurringCostRepository.save(recurringCost);
        for (Expense recurringCostExpense : recurringCost.getExpenses()) {
            recurringCostExpense.setRecurringCost(recurringCost);
            expenseService.save(recurringCostExpense);
        }
        return persistedRecurringCost;
    }

    private RecurringCost createRecurringCost(CounterPart counterPart, RecurringOption recurringOption) {
        return RecurringCost.builder()
                .counterPart(counterPart)
                .recurringOption(recurringOption)
                .expenses(new ArrayList<>())
                .build();
    }
}
