package com.bz.kafka.reactor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;
    private final StorageService storageService;


    public Flux<String> getMessages() {
        return storageService.getMessages();
    }

    public Mono<Void> deleteMessages() {
        return storageService.deleteMessages();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void consumeMessages() {
        reactiveKafkaConsumerTemplate
                .receiveAutoAck()
                .doOnNext(record -> log.info("Received message key - {}, value - {}", record.key(), record.value()))
                .flatMap(record -> storageService.addMessage(record.value()))
                .onErrorContinue((e, record) -> log.error("Error occurred while consuming record: {}", record, e))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

}
