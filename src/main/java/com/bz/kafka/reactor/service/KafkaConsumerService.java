package com.bz.kafka.reactor.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class KafkaConsumerService {

    private static final List<String> MESSAGES = new CopyOnWriteArrayList<>();

    @KafkaListener(topics = "my-topic", groupId = "my-consumer-group")
    public void consume(ConsumerRecord<String, String> record) {
        log.info("Received message key - {}, value - {}", record.key(), record.value());
        MESSAGES.add(record.value());
    }

    @KafkaListener(topics = "my-topic", groupId = "my-consumer-group-2")
    public void consumeAnotherConsumer(ConsumerRecord<String, String> record) {
        log.info("Received message key (2nd consumer) - {}, value - {}", record.key(), record.value());
    }

    public List<String> getMessages() {
        return MESSAGES;
    }

}
