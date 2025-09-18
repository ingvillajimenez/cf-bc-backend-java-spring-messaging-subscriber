package com.pubsub.subscriber.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pubsub.subscriber.model.Body;
import com.pubsub.subscriber.model.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Base64;

// PubSubController consumes a Pub/Sub message
@Slf4j
@RestController
@RequiredArgsConstructor
public class PubSubController {

    private final ObjectMapper objectMapper;

    @PostMapping("/")
    public Mono<ResponseEntity<String>> receiveMessage(@RequestBody Body body) {
        // Get PubSub message from request body
        Body.Message message = body.getMessage();
        if (message == null) {
            String msg = "Bad Request: invalid Pub/Sub message format";
            log.error(msg);
            return Mono.just(new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST));
        }

        String data = message.getData();
        UserMessage userMessage;
        try {
            userMessage = objectMapper.readValue(new String(Base64.getDecoder().decode(data)), UserMessage.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }

        log.info("UserMessage info codigofacilito: {}", userMessage);
        return Mono.just(new ResponseEntity<>(userMessage.getBody(), HttpStatus.OK));
    }
}
// [END cloud run pubsub handler]