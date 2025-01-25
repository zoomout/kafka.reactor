package com.bz.kafka.reactor.controller;

import com.bz.kafka.reactor.service.KafkaConsumerService;
import com.bz.kafka.reactor.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/messages")
@RequiredArgsConstructor
public class MessageController {

    private final KafkaProducerService kafkaProducerService;
    private final KafkaConsumerService kafkaConsumerService;

    @PostMapping
    public String sendMessage(@RequestBody String message) {
        kafkaProducerService.sendMessage("my-topic", message);
        return "OK";
    }

    @GetMapping
    public List<String> getMessages() {
        return kafkaConsumerService.getMessages();
    }

}
