package org.tenpo.test.mstenpotest.multiply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MultiplyService {

    ResultResponse multiply(MultiplyRequest multiplyRequest);

    void save(MultiplyEntity multiplyEntity);

    Page<MultiplyResponse> getMultiplyHistoryPages(Pageable Pageable);
}
