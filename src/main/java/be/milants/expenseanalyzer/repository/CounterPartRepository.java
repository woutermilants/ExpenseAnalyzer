package be.milants.expenseanalyzer.repository;

import be.milants.expenseanalyzer.data.CounterPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CounterPartRepository extends JpaRepository<CounterPart, Long> {

    Optional<CounterPart> findByAccountNumber(String accountNumber);
}
