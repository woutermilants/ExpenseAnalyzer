package be.milants.expenseanalyzer.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="counterpart", nullable=false)
    private CounterPart counterPart;
    private String description;
    @OneToMany(mappedBy = "recurringCost")
    private List<Expense> expenses;
    private RecurringOption recurringOption;
}

