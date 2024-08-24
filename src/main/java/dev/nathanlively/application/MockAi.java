package dev.nathanlively.application;

import dev.nathanlively.application.port.AiGateway;
import reactor.core.publisher.Flux;

public class MockAi implements AiGateway {
    @Override
    public Flux<String> sendMessageAndReceiveReplies(String message, String chatId) {
        // when message is "comm check" then respond with "good check"

        return Flux.just("Hello!");
    }
}
