package mn.michal.onlineshopapp.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderInputDTO {
    private static final String ERROR_MESSAGE = "An order should have at least one order line";
    @NotNull(message = ERROR_MESSAGE)
    @Valid
    private List<OrderLineInputDTO> orderLines;
}
