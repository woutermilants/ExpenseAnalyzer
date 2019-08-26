package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.Direction;
import be.milants.expenseanalyzer.data.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ExpenseService expenseService;

    public Map<String, List<Expense>> recurringPayments() {
        final Map<String, List<Expense>> groupedByCounterPart = expenseService.getGroupedByCounterPart(Direction.COST);
        Map<String, List<Expense>> recurringPayments = groupedByCounterPart.entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 2)
                .filter(entry -> paymentOccursInAtLeastXConsecutiveYears(entry.getValue(), 2))
                .filter(entry -> amountIsAlmostTheSame(entry.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        return recurringPayments;
    }

    private boolean amountIsAlmostTheSame(List<Expense> expenses) {
        return true;
    }

    private boolean paymentOccursInAtLeastXConsecutiveYears(List<Expense> expenses, int requiredNumberOfConsecutiveYears) {
        Map<Integer, List<Expense>> expensesByYear = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        for (Expense expense : expenses) {
            calendar.setTime(expense.getDate());
            final int year = calendar.get(Calendar.YEAR);
            if (!expensesByYear.containsKey(year)) {
                expensesByYear.put(year, new ArrayList<>());
            }
            expensesByYear.get(year).add(expense);
        }

        LinkedHashMap<Integer, List<Expense>> sortedExpensesByYear =
                expensesByYear.entrySet()
                        .stream()
                        .sorted(comparingByKey())
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        int numberOfConsecutiveYears = 1;
        final Iterator<Map.Entry<Integer, List<Expense>>> iterator = sortedExpensesByYear.entrySet().iterator();
        int lastYearExpenseOccurred = iterator.next().getKey();
        while (iterator.hasNext()) {
            final Integer nextYear = iterator.next().getKey();
            if (lastYearExpenseOccurred == nextYear + 1) {
                numberOfConsecutiveYears++;
                if (numberOfConsecutiveYears >= requiredNumberOfConsecutiveYears) {
                    return true;
                }
            } else {
                lastYearExpenseOccurred = nextYear;
                numberOfConsecutiveYears = 1;
            }
        }
        return false;
    }
}
