package dev.nathanlively.application;

import dev.nathanlively.adapter.in.web.droidcomm.UserMessageDto;
import dev.nathanlively.application.port.AiGateway;
import reactor.core.publisher.Flux;

public class AiService {
    private final AiGateway aiGateway;

    public AiService(AiGateway aiGateway) {
        this.aiGateway = aiGateway;
    }

    public Flux<String> sendMessageAndReceiveReplies(UserMessageDto userMessageDto) {
        return aiGateway.sendMessageAndReceiveReplies(userMessageDto);
    }
}
