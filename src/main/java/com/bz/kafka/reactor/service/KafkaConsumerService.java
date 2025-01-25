package com.bz.kafka.reactor.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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

    public Flux<String> getMessages() {
        return Flux.fromIterable(MESSAGES);
    }

}
