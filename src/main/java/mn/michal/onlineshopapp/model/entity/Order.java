package mn.michal.onlineshopapp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    @NotNull
    private LocalDateTime dateTime;
    @NotNull
    private double amount;
    @Enumerated(EnumType.ORDINAL)
    private StatusType status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLine> orderLines = new ArrayList<>();

    @PrePersist
    public void assignDateTime() {
        dateTime = LocalDateTime.now();
    }

    public enum StatusType {
        PENDING,
        SENT,
        COMPLETED,
        CANCELED
    }
}
