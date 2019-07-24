package be.milants.expenseanalyzer.repository;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.data.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
}
