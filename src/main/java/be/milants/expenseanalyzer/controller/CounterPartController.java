package be.milants.expenseanalyzer.controller;

import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import be.milants.expenseanalyzer.service.CounterPartService;
import be.milants.expenseanalyzer.util.PageRequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/counterparts")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CounterPartController {

    private final CounterPartService counterPartService;

    @GetMapping
    public Page<CounterPartDto> getAllCounterParts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sortColumn,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return counterPartService.findAll(PageRequestUtil.getPageRequest(page, size, sortColumn, direction));
    }

/*    @GetMapping(value = "/counterparts/{id}/totalIncome")
    public Double getTotalForCounterPart(@PathVariable Long id) {
        return counterPartService.calculateTotalForCounterPart(id, Direction.COST);
    }*/

    @PutMapping(value = "/{accountNumber}")
    public CounterPartDto updateCounterpart(@PathVariable String accountNumber,
                                            @RequestBody CounterPartDto counterPartDto) {
        log.info("Updating counterpart with accountNumber {}", accountNumber);
        return counterPartService.updateCounterpart(accountNumber, counterPartDto);
    }
}
