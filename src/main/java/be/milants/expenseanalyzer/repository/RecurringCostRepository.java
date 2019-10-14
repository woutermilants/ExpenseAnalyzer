package be.milants.expenseanalyzer.repository;

import be.milants.expenseanalyzer.data.RecurringCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecurringCostRepository extends JpaRepository<RecurringCost, Long> {
}
