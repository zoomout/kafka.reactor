package com.bz.kafka.reactor;

import com.bz.kafka.reactor.config.TestSetupConfiguration;
import com.bz.kafka.reactor.service.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import lombok.val;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Import({TestSetupConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
class KafkaReactorApplicationIntegrationTest {

    @MockitoSpyBean
    StorageService service;

    @BeforeEach
    void cleanup() {
        RestAssured
                .given()
                .body("MyMessage")
                .when().delete("/messages")
                .then().statusCode(200);
    }

    @Test
    void healthCheck() {
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            RestAssured
                    .when().get("/actuator/health")
                    .then().statusCode(200);
        });
    }

    @Test
    void sendAndGetMessage() {
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

    @Test
    void sendAndGetMultipleMessages() {
        RestAssured
                .given()
                .body("MyMessage1")
                .when().post("/messages")
                .then().statusCode(200);
        RestAssured
                .given()
                .body("MyMessage2")
                .when().post("/messages")
                .then().statusCode(200);
        Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            String messages = RestAssured
                    .when().get("/messages")
                    .then().statusCode(200)
                    .extract().body().asString();
            assertThat(messages).isEqualTo("[\"MyMessage1\",\"MyMessage2\"]");
        });
    }

    @Test
    void testIsAbleToRecoverAfterFailure() {
        val failingMessage = "BadMessage";
        when(service.addMessage(failingMessage)).thenReturn(Mono.error(new RuntimeException("Oh no - " + failingMessage)));

        RestAssured
                .given()
                .body("GoodMessage1")
                .when().post("/messages");
        RestAssured
                .given()
                .body(failingMessage)
                .when().post("/messages")
                .then().statusCode(200);
        RestAssured
                .given()
                .body("GoodMessage2")
                .when().post("/messages")
                .then().statusCode(200);

        Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            val body = RestAssured
                    .when().get("/messages")
                    .then().statusCode(200)
                    .extract().body().asString();
            val actual = asListOfStrings(body);
            assertThat(actual).hasSize(2);
            assertThat(actual).contains("GoodMessage1");
            assertThat(actual).contains("GoodMessage2");
        });
    }

    private static List<String> asListOfStrings(String body) throws JsonProcessingException {
        return new ObjectMapper().readValue(body, new TypeReference<>() {
        });
    }

}
