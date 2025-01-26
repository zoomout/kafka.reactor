package com.bz.kafka.reactor.config;

import lombok.val;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

import java.util.List;

import static org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.TRANSACTIONAL_ID_CONFIG;

@Configuration
public class ReactiveKafkaConfig {

    public static final String KAFKA_TOPIC = "my-topic";

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate(
            KafkaProperties properties
    ) {
        val stringObjectMap = properties.buildProducerProperties();
        stringObjectMap.put(TRANSACTIONAL_ID_CONFIG, "tx-");
        stringObjectMap.put(ENABLE_IDEMPOTENCE_CONFIG, "true");
        return new ReactiveKafkaProducerTemplate<>(
                SenderOptions.create(stringObjectMap)
        );
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate(
            KafkaProperties properties
    ) {
        return new ReactiveKafkaConsumerTemplate<>(
                ReceiverOptions.<String, String>create(properties.buildConsumerProperties())
                        .subscription(List.of(KAFKA_TOPIC))
        );
    }

}
