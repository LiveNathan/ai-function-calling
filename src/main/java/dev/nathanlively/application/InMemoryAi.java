package dev.nathanlively.application;

import dev.nathanlively.application.port.AiGateway;
import reactor.core.publisher.Flux;

public class InMemoryAi implements AiGateway {
    @Override
    public Flux<String> sendMessageAndReceiveReplies(String message) {
        return Flux.just("Hello!");
    }
}
