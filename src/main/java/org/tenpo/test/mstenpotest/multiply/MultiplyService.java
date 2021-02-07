package org.tenpo.test.mstenpotest.multiply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MultiplyService {

    ResultResponse multiply(MultiplyRequest multiplyRequest);

    Page<MultiplyResponse> getMultiplyHistoryPages(Pageable Pageable);
}
