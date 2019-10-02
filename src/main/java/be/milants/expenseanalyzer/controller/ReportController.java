package be.milants.expenseanalyzer.controller;

import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/reports")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {

    private final ReportService reportService;

    @GetMapping(path = "/recurringPayments")
    public Map<String, List<Expense>> getRecurringPayments() {
        return reportService.recurringPayments();
    }
}
