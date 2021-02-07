package org.tenpo.test.mstenpotest.multiply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiplyRequest {
    @NotNull
    private BigDecimal numberA;
    @NotNull
    private BigDecimal numberB;
}
