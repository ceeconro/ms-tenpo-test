package org.tenpo.test.mstenpotest.multiply;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@Api("API that allows multiply two numbers")
public class MultiplyController {


    private final MultiplyService multiplyService;

    @Autowired
    public MultiplyController(MultiplyService multiplyService) {
        this.multiplyService = multiplyService;
    }

    @ApiOperation(value = "${api.ms-tenpo-test.multiply.description}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request, invalid format of the request."),
            @ApiResponse(code = 401, message = "Unauthorized, invalid credentials to access the resource.")
    })
    @PostMapping("/multiply")
    public ResponseEntity multiplyTwoNumbers(@Valid @RequestBody MultiplyRequest multiplyRequest) {
        return ResponseEntity.ok(multiplyService.multiply(multiplyRequest));
    }

    @GetMapping("/multiply/history")
    public ResponseEntity listMultiplyHistory(Pageable pageable) {
        log.debug("Pageable: {}", pageable);

        return ResponseEntity.ok(multiplyService.getMultiplyHistoryPages(pageable));
    }
}
