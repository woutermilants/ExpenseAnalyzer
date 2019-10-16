package be.milants.expenseanalyzer.repository;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.RecurringCost;
import be.milants.expenseanalyzer.data.RecurringOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecurringCostRepository extends JpaRepository<RecurringCost, Long> {
    Optional<RecurringCost> findByCounterPartAndRecurringOption(CounterPart counterPart, RecurringOption recurringOption);
}
