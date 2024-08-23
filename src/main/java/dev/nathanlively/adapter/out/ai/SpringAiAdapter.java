package dev.nathanlively.adapter.out.ai;

import dev.nathanlively.application.port.AiGateway;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;

public class SpringAiAdapter implements AiGateway {
    private final ChatClient chatClient;

    public SpringAiAdapter(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Flux<String> sendMessageAndReceiveReplies(String message) {
        return chatClient
                .prompt()
                .user(message)
                .stream()
                .content();
    }
}
