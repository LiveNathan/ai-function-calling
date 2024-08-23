package dev.nathanlively.config;

import dev.nathanlively.adapter.out.ai.SpringAiAdapter;
import dev.nathanlively.application.AiService;
import dev.nathanlively.application.port.AiGateway;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public AiService aiService(AiGateway aiGateway) {
        return new AiService(aiGateway);
    }

    @Bean
    public AiGateway aiGateway(ChatClient chatClient) {
        return new SpringAiAdapter(chatClient);
    }
}
