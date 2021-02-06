package org.tenpo.test.mstenpotest.multiply;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class ResultResponse {
    private BigDecimal result;
}
