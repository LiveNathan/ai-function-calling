package dev.nathanlively.config;

import dev.nathanlively.adapter.out.ai.SpringAiAdapter;
import dev.nathanlively.application.*;
import dev.nathanlively.application.port.AiGateway;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.RequestRepository;
import dev.nathanlively.application.port.ResourceRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public AiService aiService(ChatClient.Builder chatClientBuilder, ProjectRepository projectRepository, VectorStore vectorStore) {
        AiGateway gateway = new SpringAiAdapter(chatClientBuilder, projectRepository, vectorStore);
        return new AiService(gateway);
    }

    @Bean
    public ResourceRepository resourceRepository() {
        return InMemoryResourceRepository.createEmpty();
    }

    @Bean
    public ProjectRepository projectRepository() {
        return InMemoryProjectRepository.createEmpty();
    }

    @Bean
    public RequestRepository requestRepository() {
        return InMemoryRequestRepository.createEmpty();
    }

    @Bean
    public ClockInService clockInService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        return new ClockInService(resourceRepository, projectRepository);
    }

    @Bean
    public UnfulfilledRequestService unfulfilledRequestService(VectorStore vectorStore, RequestRepository requestRepository) {
        return new UnfulfilledRequestService(vectorStore, requestRepository);
    }
}
