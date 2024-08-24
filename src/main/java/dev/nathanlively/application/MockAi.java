package dev.nathanlively.application;

import dev.nathanlively.application.port.AiGateway;
import reactor.core.publisher.Flux;

public class MockAi implements AiGateway {
    @Override
    public Flux<String> sendMessageAndReceiveReplies(String message, String chatId) {
        // Return different responses based on the input message
        if ("comm check".equalsIgnoreCase(message)) {
            return Flux.just("good check");
        } else if ("weather".equalsIgnoreCase(message)) {
            return Flux.just("The weather is sunny");
        } else {
            return Flux.just("Hello!");
        }
    }
}
