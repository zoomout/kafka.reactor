package com.bz.kafka.reactor.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class StorageService {

    private static final List<String> MESSAGES = new CopyOnWriteArrayList<>();

    public Flux<String> getMessages() {
        return Flux.defer(() -> Flux.fromIterable(MESSAGES));
    }

    public Mono<Void> deleteMessages() {
        return Mono.defer(() -> Mono.fromRunnable(MESSAGES::clear));
    }

    public Mono<String> addMessage(String message) {
        return Mono.defer(() -> Mono.just(message)
                .doOnNext(MESSAGES::add)
        );
    }
}
