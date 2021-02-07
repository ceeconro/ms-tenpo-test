package org.tenpo.test.mstenpotest.multiply;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.tenpo.test.mstenpotest.exceptions.InvalidInputException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MultiplyServiceImplSpec {

    @Autowired
    private MultiplyService multiplyService;

    @MockBean
    private MultiplyRepository multiplyRepository;

    @Test
    @DisplayName("Given an multiply request, when aply the operation, then will return the result")
    public void multiplyAndReturnResult() {
        ResultResponse resultResponse = multiplyService.multiply(new MultiplyRequest(new BigDecimal(2), new BigDecimal(4)));

        assertEquals(new ResultResponse(new BigDecimal(8)), resultResponse);
    }

    @Test
    @DisplayName("Given an multiply request, then will send to persist")
    public void multiplyAndPersist() {

        MultiplyRequest multiplyRequest = new MultiplyRequest(new BigDecimal(2), new BigDecimal(4));
        ResultResponse resultResponse = multiplyService.multiply(multiplyRequest);
        verify(multiplyRepository, times(1)).save(any());

    }

    @Test
    @DisplayName("Given an empty multiply request, then will throw invalid input exception")
    public void multiplyEmptyValuesThrowException() {

        MultiplyRequest multiplyRequest = new MultiplyRequest();
        assertThrows(InvalidInputException.class, () -> multiplyService.multiply(multiplyRequest));

    }


}