package com.bz.kafka.reactor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(String topic, String message) {
        try {
            val result = kafkaTemplate.send(topic, UUID.randomUUID().toString(), message).get();
            val sentKey = result.getProducerRecord().key();
            log.info("Sent message key - {}, value - {}", sentKey, message);
            return sentKey;
        } catch (InterruptedException | ExecutionException e) {
            log.error("Couldn't send message", e);
            throw new RuntimeException(e);
        }

    }

}
