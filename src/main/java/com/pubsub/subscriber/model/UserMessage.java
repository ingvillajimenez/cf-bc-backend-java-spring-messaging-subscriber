package com.pubsub.subscriber.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserMessage {

    private String body;
    private String username;
    private LocalDateTime createdAt;
}
