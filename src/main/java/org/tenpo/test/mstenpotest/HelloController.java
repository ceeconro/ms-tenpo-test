package org.tenpo.test.mstenpotest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tenpo.test.mstenpotest.multiply.MultiplyEntity;

import java.math.BigDecimal;

@RestController
public class HelloController {
    @Autowired
    KafkaMessageProducer kafkaMessageProducer;

    @GetMapping("/hello")
    public String hello(){
        return "Hello you";
    }

    @PostMapping("/add/{topic}")
    public void addIdCustomer(@PathVariable String topic, @RequestBody String body)
    {
        kafkaMessageProducer.sendMessage(new MultiplyEntity(BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(2)));
    }
}
