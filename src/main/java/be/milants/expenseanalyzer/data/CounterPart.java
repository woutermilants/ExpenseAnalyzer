package be.milants.expenseanalyzer.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CounterPart {

    @Id
    @Column(length = 40)
    private String accountNumber;
    private String name;
    private boolean recurringCounterPart;
    private boolean ownAccount;

    public CounterPart(String accountNumber, String name) {
        this.accountNumber = accountNumber;
        this.name = name;
    }
}
