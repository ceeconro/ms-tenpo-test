package org.tenpo.test.mstenpotest.multiply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiplyResponse {

    private BigDecimal numberA;
    private BigDecimal numberB;
    private BigDecimal result;
}
