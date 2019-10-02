package be.milants.expenseanalyzer.controller;

import be.milants.expenseanalyzer.expense.rest.model.ExpenseDto;
import be.milants.expenseanalyzer.service.ExpenseService;
import be.milants.expenseanalyzer.util.PageRequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/expenses")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping(path = "/")
    public Page<ExpenseDto> getAllExpenses(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sortColumn,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return expenseService.getAllExpenses(PageRequestUtil.getPageRequest(page, size, sortColumn, direction));
    }
}
