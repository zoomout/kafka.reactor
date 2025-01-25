package com.bz.kafka.reactor;

import com.bz.kafka.reactor.config.TestDockerConfiguration;
import io.restassured.RestAssured;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.ComposeContainer;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestDockerConfiguration.class)
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

    @Test
    void sendMessageAndGetMessages() {
        RestAssured
                .given()
                .body("MyMessage")
                .when().post("/messages")
                .then().statusCode(200);
        Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            String messages = RestAssured
                    .when().get("/messages")
                    .then().statusCode(200)
                    .extract().body().asString();
            assertThat(messages).isEqualTo("[\"MyMessage\"]");
        });
    }


}
