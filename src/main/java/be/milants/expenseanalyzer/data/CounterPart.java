package be.milants.expenseanalyzer.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CounterPart {

    @Id
    @Column(length = 40)
    private String accountNumber;
    private String name;
    private boolean recurringCounterPart;
    private boolean ownAccount;
    @JsonIgnore
    @OneToMany(mappedBy = "counterPart")
    private List<Expense> expenses;
    @JsonIgnore
    @OneToMany(mappedBy = "counterPart")
    private List<RecurringCost> recurringCosts;

    public CounterPart(String accountNumber, String name) {
        this.accountNumber = accountNumber;
        this.name = name;
    }
}
