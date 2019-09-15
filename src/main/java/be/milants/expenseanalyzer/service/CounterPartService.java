package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.repository.CounterPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounterPartService {

    @Autowired
    private CounterPartRepository counterPartRepository;

    public void create(String accountNumber, String name) {
        counterPartRepository.save(new CounterPart(accountNumber, name));
    }
}
