package org.tenpo.test.mstenpotest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.tenpo.test.mstenpotest.multiply.MultiplyEntity;
import org.tenpo.test.mstenpotest.multiply.MultiplyService;

@Component
@Slf4j
public class KafkaTestListener {

    private final MultiplyService multiplyService;

    @Autowired
    public KafkaTestListener(MultiplyService multiplyService) {
        this.multiplyService = multiplyService;
    }

    @KafkaListener(topics = "${message.topic.name}", groupId = "${message.group.name}")
    public void listenTopic1(MultiplyEntity message) {
        log.info("Recieved Message of topic1 in  listener: {}", message);
        multiplyService.save(message);
    }
}
