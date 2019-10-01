package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Direction;
import be.milants.expenseanalyzer.data.Expense;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ExpenseService expenseService;
    private final CounterPartService counterPartService;

    public void logRecurring() {
        final Map<String, List<Expense>> stringListMap = recurringPayments();
        for (String key : stringListMap.keySet()) {
            try {
                final String counterPartName = counterPartService.findByAccountNumber(key).getName();
                log.info(key + " " + counterPartName);
                log.info(" ");
                for (Expense expense : stringListMap.get(key)) {
                    log.info(expense.getCounterPart().getAccountNumber() + " " + expense.getCounterPart().getName() + " " + expense.getAmount() + " " + new SimpleDateFormat().format(expense.getDate()));
                }
                log.info(" ");
            } catch (Exception e) {
            }
        }
    }

    public Map<String, List<Expense>> recurringPayments() {
        Map<CounterPart, List<Expense>> groupedByCounterPart = expenseService.getGroupedByCounterPart(Direction.COST);
        groupedByCounterPart.entrySet()
                .stream()
                .filter(counterPartListEntry -> counterPartListEntry.getValue().size() > 1)
                .filter(counterPartListEntry -> counterPartListEntry.getKey().isRecurringCounterPart())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        return Collections.emptyMap();

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
        if ((Math.abs(amountToCompareTo.doubleValue() * (1 - withinPercentage)) < Math.abs(currentAmount.doubleValue())) &&
                Math.abs(currentAmount.doubleValue()) < Math.abs(amountToCompareTo.doubleValue() * (1 + withinPercentage))) {
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
