package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.Direction;
import be.milants.expenseanalyzer.data.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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
                .map(this::removeAllNonRelatedValues)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        return recurringPayments;
    }

    private Map.Entry<String, List<Expense>> removeAllNonRelatedValues(Map.Entry<String, List<Expense>> value) {
        Iterator<Expense> iterator = value.getValue().iterator();
        if (iterator.hasNext()) {
            Integer amountInCents = iterator.next().getAmountInCents();
            while (iterator.hasNext()) {
                Expense currentExpense = iterator.next();
                if (areValuesClose(currentExpense.getAmountInCents(), amountInCents)) {
                    // do nothing
                } else {
                    iterator.remove();
                }
            }
        }
        return value;
    }

    private boolean areValuesClose(Integer currentAmountInCents, Integer amountInCentsToCompareTo) {
        if ((currentAmountInCents > amountInCentsToCompareTo * (double) 0.9) &&
                currentAmountInCents < amountInCentsToCompareTo * (double) 1.1) {
            return true;
        }
        return false;
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
        int firstYearOccurred = iterator.next().getKey();
        while (iterator.hasNext()) {
            final Integer nextYear = iterator.next().getKey();
            if (firstYearOccurred == nextYear - 1) {
                numberOfConsecutiveYears++;
                if (numberOfConsecutiveYears >= requiredNumberOfConsecutiveYears) {
                    return true;
                }
            } else {
                firstYearOccurred = nextYear;
                numberOfConsecutiveYears = 1;
            }
        }
        return false;
    }
}
