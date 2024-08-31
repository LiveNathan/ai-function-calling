package dev.nathanlively.adapter.out.ai;

import dev.nathanlively.adapter.in.web.droidcomm.UserMessageDto;
import dev.nathanlively.application.port.AiGateway;
import dev.nathanlively.application.port.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

public class SpringAiAdapter implements AiGateway {
    private final ChatClient chatClient;
    private final ProjectRepository projectRepository;
    private static final Logger log = LoggerFactory.getLogger(SpringAiAdapter.class);

    public SpringAiAdapter(ChatClient.Builder builder, ProjectRepository projectRepository, VectorStore vectorStore, ChatMemory chatMemory) {
        this.projectRepository = projectRepository;
        this.chatClient = builder.defaultSystem("""
                        You are an assistant project manager expert at managing many resources and schedules.
                        Adopt the user's tone to make them feel comfortable with you. If they are playful and silly, so are you. If they are professional and matter-of-fact, so are you.
                        Keep your responses short and direct because people need your help in a hurry, but for complex tasks, think out loud by writing each step.
                        For questions about long documents, pull the most relevant quote from the document and consider whether it answers the user's question or whether it lacks sufficient detail.
                        Today is {current_date}. This message was sent by {user_name} at exactly {message_creation_time}.
                        Available projects are: {available_projects}. The project name is its natural identifier.
                        When calling functions, always use the exact name of the project as provided here. For example, a user's request may reference `projct a`, `12345`, or simply `A`, but if `Project A (12345)` is on the list of available projects, then function calls should be made with `Project A (12345)`. However, if the user's request references a significantly different project name like `projct b`, `54333`, or simply `B`, then the request should be rejected.""")
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new VectorStoreChatMemoryAdvisor(vectorStore),
                        new LoggingAdvisor())
                .defaultFunctions("clockInFunction", "clockOutFunction", "findAllProjectNamesFunction", "updateProjectFunction")
                .build();
    }

    @Override
    public Flux<String> sendMessageAndReceiveReplies(UserMessageDto userMessageDto) {
        String projectNames = String.join(", ", projectRepository.findAllNames());
        return chatClient.prompt()
                .system(sp -> sp.params(Map.of(
                        "current_date", LocalDate.now().toString(),
                        "message_creation_time", userMessageDto.creationTime().toString(),
                        "user_name", userMessageDto.userName(),
                        "available_projects", projectNames
                )))
                .user(userMessageDto.userMessageText())
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, userMessageDto.chatId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .stream()
                .content()
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("WebClient request failed with status: {} and response body: {}",
                            e.getStatusCode(), e.getResponseBodyAsString(), e);
                    return Mono.error(new RuntimeException("Failed to communicate with AI service", e));
                });
    }

}
