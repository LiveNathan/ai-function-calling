package dev.nathanlively.config;

import dev.nathanlively.adapter.out.ai.SpringAiAdapter;
import dev.nathanlively.application.port.AiGateway;
import dev.nathanlively.application.AiService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public AiService aiService() {
        return new AiService();
    }

    @Bean
    public AiGateway aiGateway(ChatClient chatClient) {
        return new SpringAiAdapter(chatClient);
    }
}
