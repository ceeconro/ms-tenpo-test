package org.tenpo.test.mstenpotest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.tenpo.test.mstenpotest.multiply.MultiplyEntity;

@Component
@Slf4j
public class KafkaMessageProducer {

    private final KafkaTemplate<String, MultiplyEntity> kafkaTemplate;


    private final String topicName;

    @Autowired
    public KafkaMessageProducer(KafkaTemplate<String, MultiplyEntity> kafkaTemplate,
                                @Value("${message.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendMessage(MultiplyEntity message) {
        log.info("Sent message=[{}] with topicName=[{}]", message, topicName);
        kafkaTemplate.send(topicName, message);
    }
}
