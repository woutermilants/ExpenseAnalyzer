package be.milants.expenseanalyzer.controller;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.data.RecurringCost;
import be.milants.expenseanalyzer.data.RecurringOption;
import be.milants.expenseanalyzer.expense.rest.model.CreateRecurringCostDto;
import be.milants.expenseanalyzer.expense.rest.model.RecurringCostDto;
import be.milants.expenseanalyzer.expense.rest.model.RecurringOptionDto;
import be.milants.expenseanalyzer.service.CounterPartService;
import be.milants.expenseanalyzer.service.ExpenseService;
import be.milants.expenseanalyzer.service.RecurringCostService;
import be.milants.expenseanalyzer.service.mapper.RecurringCostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/recurringcost")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RecurringCostController {

    private final CounterPartService counterPartService;
    private final RecurringCostMapper recurringCostMapper;
    private final ExpenseService expenseService;
    private final RecurringCostService recurringCostService;

    @GetMapping
    public List<RecurringCostDto> getAllRecurringCosts() {
        return recurringCostMapper.domainToDTO(recurringCostService.findAll());
    }

    @PostMapping(path = "/counterpart/{counterPartAccountNumber}/expense/{expenseId}")
    public RecurringCostDto createCost(@PathVariable String counterPartAccountNumber,
                                       @PathVariable Long expenseId,
                                       @RequestBody RecurringOptionDto recurringOptionDto) {
        log.info("creating cost");
        final CounterPart counterPart = counterPartService.getByAccountNumber(counterPartAccountNumber);
        final Expense expense = expenseService.getExpenseById(expenseId);
        final RecurringOption recurringOption = RecurringOption.valueOf(recurringOptionDto.getRecurringOption());
        return recurringCostMapper.domainToDTO(recurringCostService.createOrAddCost(counterPart, recurringOption, expense));
    }

    @PostMapping
    public RecurringCostDto createCost(@RequestBody CreateRecurringCostDto createRecurringCostDto) {
        final RecurringCost recurringCost = recurringCostService.createRecurringCost(createRecurringCostDto.getCounterPartAccountNumber(), createRecurringCostDto.getDescription(), createRecurringCostDto.getExpenseIds(), createRecurringCostDto.getRecurringOption());
        return recurringCostMapper.domainToDTO(recurringCost);
    }
}
