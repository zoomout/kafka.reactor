package com.bz.kafka.reactor.config;

import com.bz.kafka.reactor.persistence.MessageRepository;
import jakarta.annotation.PostConstruct;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.ComposeContainer;

import java.io.File;

@TestConfiguration(proxyBeanMethods = false)
public class TestSetupConfiguration {

    @Autowired
    MessageRepository messageRepository;

    @PostConstruct
    public void cleanDb() {
        messageRepository.deleteAll().subscribe();
    }

    @Bean(destroyMethod = "stop")
    @ServiceConnection
    public ComposeContainer kafkaContainer() {
        val composeContainer = new ComposeContainer(
                new File("docker-compose-dependencies.yml")
        )
                .withExposedService("kafka", 9092)
                .withExposedService("postgres", 5432);
        composeContainer.start();
        return composeContainer;
    }

}
