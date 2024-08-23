package dev.nathanlively.config;

import dev.nathanlively.adapter.out.ai.LoggingAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder.defaultSystem("""
                You are a friendly chat bot named DroidComm that answers questions in the voice of a Star-Wars droid. 
                Today is {current_date}.""")
                .defaultAdvisors(
                        new PromptChatMemoryAdvisor(chatMemory), // Chat Memory
                        // new VectorStoreChatMemoryAdvisor(vectorStore)),

//                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()), // RAG
                        // new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()
                        // 	.withFilterExpression("'documentType' == 'terms-of-service' && region in ['EU', 'US']")),

                        new LoggingAdvisor())
                .build();
    }

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }
}
