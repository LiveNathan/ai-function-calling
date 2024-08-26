package dev.nathanlively.application;

import dev.nathanlively.config.ChatConfig;
import dev.nathanlively.config.FunctionConfig;
import dev.nathanlively.config.ServiceConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("money")
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".*")
class ClockInServiceAiTest {
    private static final Logger log = LoggerFactory.getLogger(ClockInServiceAiTest.class);
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withPropertyValues("spring.ai.openai.apiKey=" + System.getenv("OPENAI_API_KEY"))
            .withConfiguration(AutoConfigurations.of(OpenAiAutoConfiguration.class))
            .withUserConfiguration(FunctionConfig.class, ServiceConfig.class, ChatConfig.class);

    @Test
    void clockInFunctionCall() {
        contextRunner.withPropertyValues("spring.ai.openai.chat.options.model=gpt-4o-mini")
                .run(context -> {
                    OpenAiChatModel chatModel = context.getBean(OpenAiChatModel.class);
                    UserMessage userMessage = new UserMessage("clock in to project A");
                    ChatResponse response = chatModel.call(new Prompt(List.of(userMessage),
                            OpenAiChatOptions.builder().withFunction("clockInFunction").build()));
                    log.info("Response: {}", response);
                    assertThat(response.getResult().getOutput().getContent())
                            .contains("Clock-in successful");
                });
    }

}