package dev.nathanlively.application;

import dev.nathanlively.application.port.AiGateway;
import reactor.core.publisher.Flux;

public class AiService {
    private final AiGateway aiGateway;

    public AiService(AiGateway aiGateway) {
        this.aiGateway = aiGateway;
    }

    public Flux<String> sendMessageAndReceiveReplies(String userMessage) {
        return aiGateway.sendMessageAndReceiveReplies(userMessage);
    }
}
