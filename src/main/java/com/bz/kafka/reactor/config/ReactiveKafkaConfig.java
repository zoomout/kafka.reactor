package com.bz.kafka.reactor.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

import java.util.List;

@Configuration
public class ReactiveKafkaConfig {

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate(
            KafkaProperties properties
    ) {
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(properties.buildProducerProperties()));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate(
            KafkaProperties properties
    ) {
        return new ReactiveKafkaConsumerTemplate<>(
                ReceiverOptions.<String, String>create(properties.buildConsumerProperties())
                        .subscription(List.of("my-topic"))
        );
    }

}
