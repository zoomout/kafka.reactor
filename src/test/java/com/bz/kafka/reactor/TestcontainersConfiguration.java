package com.bz.kafka.reactor;

import lombok.val;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.ComposeContainer;

import java.io.File;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean(destroyMethod = "stop")
    @ServiceConnection
    public ComposeContainer kafkaContainer() {
        final ComposeContainer composeContainer = new ComposeContainer(
                new File("docker-compose-dependencies.yml")
        )
                .withExposedService("kafka", 9092)
                .withExposedService("postgres", 5432);
        composeContainer.start();
        return composeContainer;
    }

}
