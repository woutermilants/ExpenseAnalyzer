package be.milants.expenseanalyzer.service;

import be.milants.expenseanalyzer.data.Cost;
import be.milants.expenseanalyzer.data.CounterPart;
import be.milants.expenseanalyzer.repository.CostRepository;
import be.milants.expenseanalyzer.repository.CounterPartRepository;
import be.milants.expenseanalyzer.repository.ReceivingRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    @Autowired
    private CounterPartRepository counterPartRepository;
    @Autowired
    private CostRepository costRepository;
    @Autowired
    private ReceivingRepository receivingRepository;

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

    public void createReceiving(String counterPartAccount, String receivingAmount, String statement, String date, String currentBalance) {

    }
}
