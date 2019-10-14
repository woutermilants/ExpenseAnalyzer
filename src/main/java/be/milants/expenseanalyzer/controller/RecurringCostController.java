package be.milants.expenseanalyzer.controller;

import be.milants.expenseanalyzer.data.RecurringCost;
import be.milants.expenseanalyzer.expense.rest.model.CreateRecurringCostDto;
import be.milants.expenseanalyzer.expense.rest.model.RecurringCostDto;
import be.milants.expenseanalyzer.service.CounterPartService;
import be.milants.expenseanalyzer.service.ExpenseService;
import be.milants.expenseanalyzer.service.RecurringCostService;
import be.milants.expenseanalyzer.service.mapper.RecurringCostMapper;
import be.milants.expenseanalyzer.util.PageRequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping
    public RecurringCostDto createCost(@RequestBody CreateRecurringCostDto createRecurringCostDto) {
        final RecurringCost recurringCost = recurringCostService.createRecurringCost(createRecurringCostDto.getCounterPartAccountNumber(), createRecurringCostDto.getDescription(), createRecurringCostDto.getExpenseIds(), createRecurringCostDto.getRecurringOption());
        return  recurringCostMapper.domainToDTO(recurringCost);
    }
}
