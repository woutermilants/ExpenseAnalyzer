package be.milants.expenseanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class ExpenseanalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseanalyzerApplication.class, args);
	}

}
