package org.tenpo.test.mstenpotest.multiply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
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
}
