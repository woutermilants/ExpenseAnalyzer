package be.milants.expenseanalyzer.expense.rest.model;

import be.milants.expenseanalyzer.data.Direction;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class ExpenseDto {
    private Long id;
    private String accountNumber;
    private String accountName;
    private String currency;
    private Date date;
    private String description;
    private String currentBalance;
    private Integer amountInCents;
    private Direction direction;
    private String counterPartAccount;
    private String counterPartName;
    private String statement;
}
