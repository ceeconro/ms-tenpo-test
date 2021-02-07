package org.tenpo.test.mstenpotest.multiply;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.tenpo.test.mstenpotest.exceptions.InvalidInputException;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
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

    @Test
    void getMultiplyHistoryPaged() {
        Page<MultiplyEntity> multiplyEntityList = getMultiplyEntityList();
        PageRequest pageRequest = PageRequest.of(0, 3);
        when(multiplyRepository.findAll(pageRequest))
                .thenReturn(multiplyEntityList);

        Page<MultiplyResponse> multiplyResponses = multiplyService.getMultiplyHistoryPages(pageRequest);
        assertEquals(3, multiplyResponses.getSize());
    }

    private PageImpl getMultiplyEntityList() {
        return new PageImpl(Arrays.asList(new MultiplyEntity(),new MultiplyEntity(),new MultiplyEntity()));
    }
}