package dev.nathanlively.adapter.out.ai;

import dev.nathanlively.adapter.in.web.droidcomm.UserMessageDto;
import dev.nathanlively.application.port.AiGateway;
import dev.nathanlively.application.port.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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
    @Value("classpath:/promptTemplates/assistantProjectManagerPromptTemplate.st")
    Resource assistantManagerPrompt;

    public SpringAiAdapter(ChatClient.Builder builder, ProjectRepository projectRepository, VectorStore vectorStore, ChatMemory chatMemory) {
        this.projectRepository = projectRepository;
        this.chatClient = builder.defaultSystem(assistantManagerPrompt)
                .defaultFunctions("clockIn", "clockOut", "findAllProjectNames", "createProject", "createTimesheetEntry", "createTimesheetEntryWithDuration", "updateProjectHours")
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
//                        new VectorStoreChatMemoryAdvisor(vectorStore),
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
                        "message_creation_timezone", userMessageDto.creationTimezone(),
                        "user_name", userMessageDto.userName(),
                        "available_projects", projectNames
                )))
                .user(userMessageDto.userMessageText())
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, userMessageDto.chatId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .stream()
                .content()
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("WebClient request failed register status: {} and response body: {}",
                            e.getStatusCode(), e.getResponseBodyAsString(), e);
                    return Mono.error(new RuntimeException("Failed to communicate register AI service", e));
                });
    }

}
