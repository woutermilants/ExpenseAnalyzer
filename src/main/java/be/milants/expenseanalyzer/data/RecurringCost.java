package be.milants.expenseanalyzer.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecurringCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 40)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "counterPart_accountNumber", nullable = false)
    private CounterPart counterPart;
    private String description;
    @OneToMany(mappedBy = "recurringCost",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<Expense> expenses;
    private RecurringOption recurringOption;
}

