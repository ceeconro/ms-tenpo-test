package org.tenpo.test.mstenpotest.multiply;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MultiplyRequest {
    private BigDecimal numberA;
    private BigDecimal numberB;
}
