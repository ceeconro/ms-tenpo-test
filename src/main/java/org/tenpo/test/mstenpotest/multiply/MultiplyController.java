package org.tenpo.test.mstenpotest.multiply;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@Slf4j
public class MultiplyController {


    private final MultiplyService multiplyService;

    @Autowired
    public MultiplyController(MultiplyService multiplyService) {
        this.multiplyService = multiplyService;
    }

    @PostMapping("/multiply")
    public ResponseEntity multiplyTwoNumbers(@Valid @RequestBody MultiplyRequest multiplyRequest) {
        return ResponseEntity.ok(multiplyService.multiply(multiplyRequest));
    }

    @GetMapping("/multiply/history")
    public ResponseEntity listMultiplyHistory(Pageable pageable){
        log.debug("Pageable: {}", pageable);

        return ResponseEntity.ok(multiplyService.getMultiplyHistoryPages(pageable));
    }
}
