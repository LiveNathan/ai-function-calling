package dev.nathanlively.adapter.out.ai;

import dev.nathanlively.adapter.in.web.droidcomm.UserMessageDto;
import dev.nathanlively.application.port.AiGateway;
import dev.nathanlively.application.port.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
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
    private static final int INITIAL_RETRIEVE_SIZE = 100;
    private static final int MAX_RETRIEVE_SIZE = 1000;

    public SpringAiAdapter(ChatClient.Builder builder, ProjectRepository projectRepository, VectorStore vectorStore) {
        this.projectRepository = projectRepository;
        this.chatClient = builder.defaultSystem("""
                        You are a friendly chat bot named DroidComm that answers questions in the voice of a Star-Wars droid.
                        If you don't know the answer then just say that you don't know.
                        If you don't have enough information due to unclear user request then ask follow up questions until you do.
                        Today is {current_date}. This message was sent by {user_name} at exactly {message_creation_time}.
                        Available projects are: {available_projects}. The project name is its natural identifier.
                        When calling functions always use the exact name of the project as provided here. For example, a user request may reference `projct a`, `12345`, or simply `A`, but if `Project A (12345)` is on the list of available projects then function calls should be made with `Project A (12345)`. But, if the user request references a significantly different project name like `projct b`, `54333`, or simply `B` then the request should be rejected.""")
                .defaultAdvisors(
                        new VectorStoreChatMemoryAdvisor(vectorStore),
                        new LoggingAdvisor())
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
                .functions("clockInFunction", "updateProjectFunction", "findAllProjectNamesFunction")
                .user(userMessageDto.userMessageText())
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, userMessageDto.chatId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, INITIAL_RETRIEVE_SIZE))
                .stream()
                .content()
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("WebClient request failed with status: {} and response body: {}",
                            e.getStatusCode(), e.getResponseBodyAsString(), e);
                    return Mono.error(new RuntimeException("Failed to communicate with AI service", e));
                });
    }

}
