package com.bz.kafka.reactor.controller;

import com.bz.kafka.reactor.service.KafkaConsumerService;
import com.bz.kafka.reactor.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.bz.kafka.reactor.config.ReactiveKafkaConfig.KAFKA_TOPIC;

@RestController
@RequestMapping(path = "/messages")
@RequiredArgsConstructor
public class MessageController {

    private final KafkaProducerService kafkaProducerService;
    private final KafkaConsumerService kafkaConsumerService;

    @PostMapping
    public Mono<String> sendMessage(@RequestBody String message) {
        return Mono.defer(() -> kafkaProducerService.sendMessage(KAFKA_TOPIC, message));
    }

    @GetMapping
    public Mono<List<String>> getMessages() {
        return Flux.defer(kafkaConsumerService::getMessages).collectList();
    }

    @DeleteMapping
    public Mono<Void> deleteMessages() {
        return Mono.defer(kafkaConsumerService::deleteMessages);
    }

}
