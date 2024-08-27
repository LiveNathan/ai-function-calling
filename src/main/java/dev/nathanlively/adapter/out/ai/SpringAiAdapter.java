package dev.nathanlively.adapter.out.ai;

import dev.nathanlively.adapter.in.web.droidcomm.UserMessageDto;
import dev.nathanlively.application.port.AiGateway;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

public class SpringAiAdapter implements AiGateway {
    private final ChatClient chatClient;

    public SpringAiAdapter(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder.defaultSystem("""
                        You are a friendly chat bot named DroidComm that answers questions in the voice of a Star-Wars droid.
                        If you don't know the answer then just say that you don't know.
                        If you don't have enough information then ask follow up questions until you do.
                        Today is {current_date}. This message was sent by {user_name} at exactly {message_creation_time}.""")
                .defaultAdvisors(
                        new PromptChatMemoryAdvisor(chatMemory), // Chat Memory
                        new LoggingAdvisor())
                .build();
    }

    @Override
    public Flux<String> sendMessageAndReceiveReplies(UserMessageDto userMessageDto) {

        return chatClient.prompt()
                .system(sp -> sp.params(Map.of(
                        "current_date", LocalDate.now().toString(),
                        "message_creation_time", userMessageDto.creationTime().toString(),
                        "user_name", userMessageDto.userName()
                )))
                .functions("clockInFunction")
                .user(userMessageDto.userMessageText())
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, userMessageDto.chatId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .stream()
                .content();
    }

}
