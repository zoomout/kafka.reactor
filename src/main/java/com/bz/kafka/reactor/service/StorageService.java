package com.bz.kafka.reactor.service;

import com.bz.kafka.reactor.persistence.MessageRepository;
import com.bz.kafka.reactor.persistence.dao.MessageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final MessageRepository messageRepository;

    public Flux<String> getMessages() {
        return messageRepository.findAll()
                .doOnNext(message -> log.info("Retrieved saved message: {}", message))
                .map(MessageEntity::getMessage);
    }

    public Mono<Void> deleteMessages() {
        return messageRepository.deleteAll();
    }

    public Mono<String> addMessage(String message) {
        return messageRepository.save(
                        MessageEntity.builder()
                                .message(message)
                                .build()
                )
                .map(MessageEntity::getMessage);
    }

}
