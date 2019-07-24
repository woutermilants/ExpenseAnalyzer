package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.Cost;
import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Direction;
import be.milants.expenseanalyzer.data.Expense;
import be.milants.expenseanalyzer.repository.CostRepository;
import be.milants.expenseanalyzer.repository.CounterPartRepository;
import be.milants.expenseanalyzer.repository.ExpenseRepository;
import be.milants.expenseanalyzer.repository.IncomeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ExpenseService {

    @Autowired
    private CounterPartRepository counterPartRepository;
    @Autowired
    private CostRepository costRepository;
    @Autowired
    private IncomeRepository incomeRepository;
    @Autowired
    private ExpenseRepository expenseRepository;

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

        expenseRepository.save(expense);
    }


    public void getExpensesByMonth() {
        Map<String, List<Expense>> monthExpenses = new HashMap<>();
        List<Expense> expenses = expenseRepository.findAll();


        expenses.stream().filter(expense -> expense.getDirection().equals(Direction.COST)).forEach(expense -> addToMap(monthExpenses, expense));

        System.out.println(monthExpenses);

        monthExpenses.forEach((key, value) -> System.out.println(key + " : " + value.stream().map(Expense::getAmountInCents).reduce(0, (a,b)-> a+b).intValue()/100));


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
        return localDate.getMonthValue() +"/" + localDate.getYear();
    }
}
