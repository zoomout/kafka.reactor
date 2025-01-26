package com.bz.kafka.reactor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application to send and receive Kafka messages and persist into Database
 */
@SpringBootApplication
public class KafkaReactorApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaReactorApplication.class, args);
    }

}
