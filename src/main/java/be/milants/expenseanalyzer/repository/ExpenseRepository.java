package be.milants.expenseanalyzer.repository;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByCounterPartAndDateAndStatementAndCurrentBalance(CounterPart counterPart, Date date, String statement, String currentBalance);

    List<Expense> findByCounterPart(CounterPart counterpart);
}
