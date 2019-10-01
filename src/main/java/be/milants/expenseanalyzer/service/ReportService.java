package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.Direction;
import be.milants.expenseanalyzer.data.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ExpenseService expenseService;

    public Map<String, List<Expense>> recurringPayments() {
        //final Map<String, List<Expense>> groupedByCounterPart = expenseService.getGroupedByCounterPart(Direction.COST);
return null;
/*        groupedByCounterPart.entrySet()
                .stream()
                .filter(Expense::)*/




/*        return groupedByCounterPart.entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 2)
                .filter(entry -> paymentOccursInAtLeastXConsecutiveYears(entry.getValue(), 2))
                .map(this::removeAllNonRelatedValues)
                .filter(this::removeListsWith1Entry)
                .filter(entry -> paymentOccursInAtLeastXConsecutiveYears(entry.getValue(), 2))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));*/


    }

    private boolean removeListsWith1Entry(Map.Entry<String, List<Expense>> entry) {
        return entry.getValue().size() > 1;
    }

    private Map.Entry<String, List<Expense>> removeAllNonRelatedValues(Map.Entry<String, List<Expense>> value) {
        List<Expense> newExpenses = new ArrayList<>();
        Iterator<Expense> iterator = value.getValue().iterator();
        if (iterator.hasNext()) {
            final Expense toCompareTo = iterator.next();
            BigDecimal amount = toCompareTo.getAmount();
            newExpenses.add(toCompareTo);
            while (iterator.hasNext()) {
                Expense currentExpense = iterator.next();
                if (areValuesClose(currentExpense.getAmount(), amount)) {
                    newExpenses.add(currentExpense);
                }
            }
        }
        value.setValue(newExpenses);
        return value;
    }

    private boolean areValuesClose(BigDecimal currentAmount, BigDecimal amountToCompareTo) {
        double withinPercentage = 0.2;
        if ((Math.abs(amountToCompareTo.doubleValue() * (1-withinPercentage)) < Math.abs(currentAmount.doubleValue())) &&
                Math.abs(currentAmount.doubleValue()) < Math.abs(amountToCompareTo.doubleValue() * (1+withinPercentage))) {
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
