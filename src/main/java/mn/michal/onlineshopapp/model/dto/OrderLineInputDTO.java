package mn.michal.onlineshopapp.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineInputDTO {
    @NotNull
    private Long productId;
    @NotNull
    private int quantity;
}
