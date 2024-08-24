package dev.nathanlively.adapter.out.ai;

import dev.nathanlively.application.port.AiGateway;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

public class SpringAiAdapter implements AiGateway {
    private final ChatClient chatClient;

    public SpringAiAdapter(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Flux<String> sendMessageAndReceiveReplies(String message, String chatId) {
        return chatClient.prompt()
                .system(systemSpec -> systemSpec.param("current_date", LocalDate.now().toString()))
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .stream()
                .content();
    }

}
