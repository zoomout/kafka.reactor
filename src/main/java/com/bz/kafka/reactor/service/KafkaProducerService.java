package com.bz.kafka.reactor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderRecord;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;

    public Mono<String> sendMessage(String topic, String message) {

        return Mono.fromCallable(() -> UUID.randomUUID().toString())
                .flatMap(key -> reactiveKafkaProducerTemplate
                        .sendTransactionally(SenderRecord.create(new ProducerRecord<>(topic, key, message), UUID.randomUUID().toString()))
                        .doOnSuccess(result -> log.info("Sent message key - {}, value - {}", key, message))
                        .doOnError(e -> log.error("Couldn't send message", e))
                        .map(r -> key)
                );
    }

}
