package be.milants.expenseanalyzer.controller;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Direction;
import be.milants.expenseanalyzer.expense.rest.model.CounterPartDto;
import be.milants.expenseanalyzer.service.CounterPartService;
import be.milants.expenseanalyzer.service.ExpenseService;
import be.milants.expenseanalyzer.service.mapper.MyMapper;
import be.milants.expenseanalyzer.util.PageRequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/counterparts")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CounterPartController {

    private final CounterPartService counterPartService;
    private final MyMapper myMapper;
    private final ExpenseService expenseService;

    @GetMapping
    public Page<CounterPartDto> getAllCounterParts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sortColumn,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        log.info("getting all counterpart");
        final Page<CounterPartDto> counterPartDtos = convertPage(counterPartService.findAll(PageRequestUtil.getPageRequest(page, size, sortColumn, direction)));

        counterPartDtos.stream()
                .forEach(this::calculateTotalsForCounterPart);
        return counterPartDtos;
    }

    @PutMapping(value = "/{accountNumber}")
    public CounterPartDto updateCounterpart(@PathVariable String accountNumber,
                                            @RequestBody CounterPartDto counterPartDto) {
        log.info("Updating counterpart with accountNumber {}", accountNumber);
        final CounterPart counterPart = myMapper.dtoToDomain(counterPartDto);
        return myMapper.domainToDTO(counterPartService.updateCounterpart(accountNumber, counterPart));
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

    private void calculateTotalsForCounterPart(CounterPartDto counterPartDto) {
        counterPartDto.setTotalAmountSpent(expenseService.getTotalPerCounterPart(counterPartDto.getAccountNumber(), Direction.COST));
        counterPartDto.setTotalAmountReceived(expenseService.getTotalPerCounterPart(counterPartDto.getAccountNumber(), Direction.INCOME));
    }

}
