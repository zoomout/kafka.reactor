package com.bz.kafka.reactor;

import io.restassured.RestAssured;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.ComposeContainer;

import java.time.Duration;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class KafkaReactorApplicationIntegrationTest {

    @Autowired
    ComposeContainer composeContainer;

    @Test
    void healthCheck() {
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            RestAssured
                    .when().get("/actuator/health")
                    .then().statusCode(200);
        });
    }


}
