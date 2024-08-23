package dev.nathanlively.application;

import dev.nathanlively.application.port.AiGateway;
import reactor.core.publisher.Flux;

public class AiService {
    private AiGateway aiGateway;

    public Flux<String> sendMessageAndReceiveReplies(String userMessage) {
        return aiGateway.sendMessageAndReceiveReplies(userMessage);
    }
}
