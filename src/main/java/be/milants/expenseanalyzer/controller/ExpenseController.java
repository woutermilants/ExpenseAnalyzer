package be.milants.expenseanalyzer.controller;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.expense.rest.model.ExpenseDto;
import be.milants.expenseanalyzer.service.CounterPartService;
import be.milants.expenseanalyzer.service.ExpenseService;
import be.milants.expenseanalyzer.service.mapper.ExpenseMapper;
import be.milants.expenseanalyzer.util.PageRequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/expenses")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseMapper expenseMapper;
    private final CounterPartService counterPartService;

    @GetMapping
    public Page<ExpenseDto> getAllExpenses(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sortColumn,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return convertPage(expenseService.getAllExpenses(PageRequestUtil.getPageRequest(page, size, sortColumn, direction)));
    }

    @GetMapping(path = "/counterpart/{accountNumber}")
    public List<ExpenseDto> getAllExpenses(@PathVariable String accountNumber) {
        final Optional<CounterPart> optionalCounterPart = counterPartService.findByAccountNumber(accountNumber);
        if (optionalCounterPart.isPresent()) {
            return expenseMapper.domainToDTO(expenseService.getAllExpenses(optionalCounterPart.get()));
        }
        return Collections.emptyList();
    }

    private Page<ExpenseDto> convertPage(Page<Expense> page) {
        List<Expense> cameras = page.getContent();
        return new PageImpl<>(
                cameras.stream()
                        .map(expenseMapper::domainToDTO)
                        .collect(Collectors.toList()),
                page.getPageable(),
                page.getTotalElements()
        );
    }

}
