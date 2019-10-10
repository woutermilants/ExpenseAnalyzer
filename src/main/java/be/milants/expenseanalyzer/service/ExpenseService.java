package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Direction;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.repository.CounterPartRepository;
import be.milants.expenseanalyzer.repository.ExpenseRepository;
import be.milants.expenseanalyzer.service.mapper.ExpenseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {

    private final CounterPartRepository counterPartRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    public Page<Expense> getAllExpenses(PageRequest pageRequest) {
        return expenseRepository.findAll(pageRequest);
    }

    public List<Expense> getAllExpenses(CounterPart counterPart) {
        return expenseRepository.findByCounterPart(counterPart);
    }

    public void createExpense(String accountNumber, String accountName, String currency, String transactionDate, String description, String currentBalance, String stringAmount, Direction direction, CounterPart counterPart, String statement) throws ParseException {

        stringAmount = stringAmount.replaceAll(" ", "").replace(",", ".");
        try {
            BigDecimal amount = new BigDecimal(stringAmount);

        Date formattedTransactionDate = new SimpleDateFormat("dd/MM/yyyy").parse(transactionDate);

        Expense expense = Expense.builder()
                .accountNumber(accountNumber)
                .accountName(accountName)
                .currency(currency)
                .date(formattedTransactionDate)
                .description(description)
                .currentBalance(currentBalance)
                .amount(amount)
                .direction(direction)
                .counterPart(counterPart)
                .statement(statement)
                .build();
        if (!expenseRepository.findByCounterPartAndDateAndStatementAndCurrentBalance(
                counterPart, formattedTransactionDate, statement, currentBalance).isPresent()) {
            expenseRepository.save(expense);
        }
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }
    }


    public String getExpensesByMonth(Direction direction) {
        Map<String, List<Expense>> monthExpenses = new HashMap<>();
        List<Expense> expenses = expenseRepository.findAll();

        expenses.stream().filter(expense -> expense.getDirection().equals(direction)).forEach(expense -> addToMap(monthExpenses, expense));

        StringBuilder output = new StringBuilder();

        SortedSet<String> keys = new TreeSet<>(monthExpenses.keySet());
        for (String key : keys) {
            output.append(key).append(" : ").append(monthExpenses.get(key).stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)).doubleValue()).append("\n");
        }

        return output.toString();
    }

    public BigDecimal getTotalPerCounterPart(String accountNumber, Direction direction) {
        final Optional<CounterPart> counterPart = counterPartRepository.findByAccountNumber(accountNumber);
        if (counterPart.isPresent()) {
            List<Expense> expenses = expenseRepository.findByCounterPart(counterPart.get());
            BigDecimal total = expenses.stream()
                    .filter(expense -> expense.getDirection().equals(direction))
                    .filter(expense -> expense.getAmount() != null)
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return total;
        }
        throw new RuntimeException();
    }

    public String getAllTotalsPerCounterPart(Direction direction) {
        Map<String, BigDecimal> counterPartIncomes = new HashMap<>();
        List<Expense> expenses = expenseRepository.findAll();

        List<Expense> incomes = expenses.stream()
                .filter(expense -> expense.getDirection().equals(direction))
                .collect(Collectors.toList());

        for (Expense income : incomes) {
            String key = income.getCounterPart().getName() + " " + income.getCounterPart().getAccountNumber();
            counterPartIncomes.putIfAbsent(key, BigDecimal.ZERO);
            counterPartIncomes.put(key, counterPartIncomes.get(key).add(income.getAmount()));
        }

        Map sortedCounterPartIncomes = MapUtil.sortByValue(counterPartIncomes);

        StringBuilder output = new StringBuilder();
        sortedCounterPartIncomes.keySet().forEach(key -> output.append(key + " : " + (int) sortedCounterPartIncomes.get(key) / 100 + "\n"));

        return output.toString();
    }


    private void addToMap(Map monthExpenses, Expense expense) {
        String monthYear = extractMonthYear(expense);

        monthExpenses.putIfAbsent(monthYear, new ArrayList<Expense>());
        ArrayList<Expense> expenses = (ArrayList<Expense>) monthExpenses.get(monthYear);
        expenses.add(expense);
        monthExpenses.put(monthYear, expenses);
    }

    private String extractMonthYear(Expense expense) {
        LocalDate localDate = expense.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return String.format("%02d", localDate.getMonthValue()) + "/" + localDate.getYear();
    }

    public Map<CounterPart, List<Expense>> getGroupedByCounterPart(Direction direction) {
        List<Expense> expenses = expenseRepository.findAll();
        Map<CounterPart, List<Expense>> groupedByCounterPart = new HashMap<>();

        List<Expense> filteredExpenses = expenses.stream()
                .filter(expense -> expense.getDirection().equals(direction))
                .collect(toList());

        for (Expense expense : filteredExpenses) {
            if (!groupedByCounterPart.containsKey(expense.getCounterPart())) {
                groupedByCounterPart.put(expense.getCounterPart(), new ArrayList<>());
            }
            List<Expense> counterPartExpenses = groupedByCounterPart.get(expense.getCounterPart());
            counterPartExpenses.add(expense);
            groupedByCounterPart.put(expense.getCounterPart(), counterPartExpenses);
        }

        return groupedByCounterPart;
    }

    public Map<String, List<Expense>> getGroupedByMonth(Direction direction) {
        List<Expense> expenses = expenseRepository.findAll();
        Map<String, List<Expense>> groupedByMonth = new HashMap<>();

        List<Expense> filteredExpenses = expenses.stream()
                .filter(expense -> expense.getDirection().equals(direction))
                .collect(toList());

        for (Expense expense : filteredExpenses) {
            final String monthKey = extractMonthYear(expense);
            if (!groupedByMonth.containsKey(monthKey)) {
                groupedByMonth.put(monthKey, new ArrayList<>());
            }
            List<Expense> monthExpenses = groupedByMonth.get(monthKey);
            monthExpenses.add(expense);
            groupedByMonth.put(monthKey, monthExpenses);
        }

        return groupedByMonth;
    }
}
