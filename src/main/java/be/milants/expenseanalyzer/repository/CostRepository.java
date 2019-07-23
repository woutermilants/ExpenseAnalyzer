package be.milants.expenseanalyzer.repository;

import be.milants.expenseanalyzer.data.Cost;
import be.milants.expenseanalyzer.data.CounterPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostRepository extends JpaRepository<Cost, Long> {
}
