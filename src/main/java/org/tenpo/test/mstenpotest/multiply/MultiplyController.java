package org.tenpo.test.mstenpotest.multiply;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class MultiplyController {


    @PostMapping("/multiply")
    public ResponseEntity multiplyTwoNumbers(@RequestBody MultiplyRequest multiplyRequest){
        return ResponseEntity.ok(new ResultResponse(multiplyRequest.getNumberA().multiply(multiplyRequest.getNumberB())));
    }
}
