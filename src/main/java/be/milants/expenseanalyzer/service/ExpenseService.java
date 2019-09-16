package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.Cost;
import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Direction;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.expense.rest.model.ExpenseDto;
import be.milants.expenseanalyzer.repository.CostRepository;
import be.milants.expenseanalyzer.repository.CounterPartRepository;
import be.milants.expenseanalyzer.repository.ExpenseRepository;
import be.milants.expenseanalyzer.repository.IncomeRepository;
import be.milants.expenseanalyzer.service.mapper.ExpenseMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CounterPartRepository counterPartRepository;
    private final CostRepository costRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    public Page<ExpenseDto> getAllExpenses(PageRequest pageRequest) {
        return convertPage(expenseRepository.findAll(pageRequest));
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

    public void createCounterPart(String counterPartAccount, String counterPartName) {
        if (StringUtils.isNotBlank(counterPartAccount) && StringUtils.isNotBlank(counterPartName)) {
            CounterPart counterPart = new CounterPart(counterPartAccount, counterPartName);

            counterPartRepository.save(counterPart);
        }
    }

    public void createCost(String counterPartAccount, String costAmount, String statement, String date, String currentBalance) {
        costRepository.save(new Cost(counterPartAccount, costAmount, statement, date
                , currentBalance));

    }

    public void createIncome(String counterPartAccount, String incomeAmount, String statement, String date, String currentBalance) {

    }

    public void createExpense(String accountNumber, String accountName, String currency, String transactionDate, String description, String currentBalance, String amount, Direction direction, String counterPartAccount, String counterPartName, String statement) throws ParseException {

        amount = amount.replace(",", "");
        int amountInEurocents = Integer.valueOf(amount);

        Date formattedTransactionDate = new SimpleDateFormat("dd/MM/yyyy").parse(transactionDate);

        Expense expense = Expense.builder()
                .accountNumber(accountNumber)
                .accountName(accountName)
                .currency(currency)
                .date(formattedTransactionDate)
                .description(description)
                .currentBalance(currentBalance)
                .amountInCents(amountInEurocents)
                .direction(direction)
                .counterPartAccount(counterPartAccount)
                .counterPartName(counterPartName)
                .statement(statement)
                .build();
        if (!expenseRepository.findByCounterPartAccountAndDateAndStatementAndCurrentBalance(
                counterPartAccount, formattedTransactionDate, statement, currentBalance).isPresent()) {
            expenseRepository.save(expense);
        }
    }


    public String getExpensesByMonth(Direction direction) {
        Map<String, List<Expense>> monthExpenses = new HashMap<>();
        List<Expense> expenses = expenseRepository.findAll();

        expenses.stream().filter(expense -> expense.getDirection().equals(direction)).forEach(expense -> addToMap(monthExpenses, expense));

        StringBuilder output = new StringBuilder();

        //monthExpenses.forEach((key, value) -> output.append(key + " : " + value.stream().map(Expense::getAmountInCents).reduce(0, (a,b)-> a+b).intValue()/100 + "\n"));

        SortedSet<String> keys = new TreeSet<>(monthExpenses.keySet());
        for (String key : keys) {
            output.append(key).append(" : ").append(monthExpenses.get(key).stream().map(Expense::getAmountInCents).reduce(0, (a, b) -> a + b).intValue() / 100).append("\n");
        }

        return output.toString();
    }

    public String getTotalPerCounterPart(Direction direction) {
        Map<String, Integer> counterPartIncomes = new HashMap<>();
        List<Expense> expenses = expenseRepository.findAll();

        List<Expense> incomes = expenses.stream()
                .filter(expense -> expense.getDirection().equals(direction))
                .collect(Collectors.toList());

        for (Expense income : incomes) {
            String key = income.getCounterPartName() + " " + income.getCounterPartAccount();
            counterPartIncomes.putIfAbsent(key, 0);
            counterPartIncomes.put(key, counterPartIncomes.get(key) + income.getAmountInCents());
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

        return String.format("%02d", localDate.getMonthValue()) +"/" + localDate.getYear();
    }

    public Map<String, List<Expense>> getGroupedByCounterPart(Direction direction) {
        List<Expense> expenses = expenseRepository.findAll();
        Map<String, List<Expense>> groupedByCounterPart = new HashMap<>();

        List<Expense> filteredExpenses = expenses.stream()
                .filter(expense -> expense.getDirection().equals(direction))
                .collect(toList());

        for (Expense expense : filteredExpenses) {
            if (!groupedByCounterPart.containsKey(expense.getCounterPartAccount())) {
                groupedByCounterPart.put(expense.getCounterPartAccount(), new ArrayList<>());
            }
            List<Expense> counterPartExpenses = groupedByCounterPart.get(expense.getCounterPartAccount());
            counterPartExpenses.add(expense);
            groupedByCounterPart.put(expense.getCounterPartAccount(), counterPartExpenses);
        }

        return groupedByCounterPart;
    }

    public void getTotalForCounterPart(Long id, Direction cost) {
    }
}
