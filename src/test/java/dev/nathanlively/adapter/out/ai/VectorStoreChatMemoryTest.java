package dev.nathanlively.adapter.out.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chroma.ChromaApi;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.ai.vectorstore.ChromaVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.testcontainers.chromadb.ChromaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;


@SpringBootTest(classes = VectorStoreChatMemoryTest.Conf.class)
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
@Testcontainers
public class VectorStoreChatMemoryTest {
    private static final Logger log = LoggerFactory.getLogger(VectorStoreChatMemoryTest.class);
    @Container
    static ChromaDBContainer chromadb = new ChromaDBContainer(DockerImageName.parse("chromadb/chroma:latest"));
    @Autowired
    protected ChatModel chatModel;
    @Autowired
    protected EmbeddingModel embeddingModel;
    @Autowired
    protected VectorStore vectorStore;

    @Test
    void functionCallTest() throws Exception {
        ChatMemory chatMemory = new InMemoryChatMemory();

        var chatClient = ChatClient.builder(chatModel).defaultSystem("""
                        You are an assistant project manager expert at managing many resources and schedules.
                        Adopt the user's tone to make them feel comfortable register you. If they are playful and silly, so are you. If they are professional and matter-of-fact, so are you.
                        Keep your responses short and direct because people need your help in a hurry, but for complex tasks, think out loud by writing each step.
                        For questions about long documents, pull the most relevant quote from the document and consider whether it answers the user's question or whether it lacks sufficient detail.
                        Today is {current_date}. This message was sent by {user_name} at exactly {message_creation_time} instant register {message_creation_timezone} timezone.
                        Available projects are: {available_projects}. The project name is its natural identifier.""")
                .defaultAdvisors(
                        new VectorStoreChatMemoryAdvisor(vectorStore))
                .build();

        String response = chatClient.prompt()
                .system(sp -> sp.params(Map.of(
                        "current_date", LocalDate.now().toString(),
                        "message_creation_time", LocalDateTime.now(),
                        "message_creation_timezone", ZoneId.systemDefault().toString(),
                        "user_name", "Alice",
                        "available_projects", List.of("Project A (12345)", "Project B (54333)")
                )))
                .user(u -> u.text("What is my name?"))
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, "696").param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 20))
                .call().content();

        log.info("Response: {}", response);

        assertThat(response).contains("Alice");

    }

    @Configuration
    @EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
    public class Conf {

        @Bean
        public OpenAiApi openAiApi() {
            String apiKey = System.getenv("OPENAI_API_KEY");
            if (!StringUtils.hasText(apiKey)) {
                throw new IllegalArgumentException(
                        "You must provide an API key.  Put it in an environment variable under the name OPENAI_API_KEY");
            }

            return new OpenAiApi(apiKey);
        }

        @Bean
        public OpenAiChatModel openAiChatModel(OpenAiApi api, FunctionCallbackContext functionCallbackContext) {
            return new OpenAiChatModel(api,
                    OpenAiChatOptions.builder().withModel(OpenAiApi.ChatModel.GPT_4_O_MINI).build(),
                    functionCallbackContext, RetryUtils.DEFAULT_RETRY_TEMPLATE);
        }

        @Bean
        public OpenAiEmbeddingModel openAiEmbeddingModel(OpenAiApi api) {
            return new OpenAiEmbeddingModel(api);
        }

        @Bean
        @ConditionalOnMissingBean
        public FunctionCallbackContext springAiFunctionManager(ApplicationContext context) {
            FunctionCallbackContext manager = new FunctionCallbackContext();
            manager.setApplicationContext(context);
            return manager;
        }

        @Bean
        public VectorStore vectorStore(EmbeddingModel embeddingModel, ChromaApi chromaApi) {
            return new ChromaVectorStore(embeddingModel, chromaApi, true);
        }

        @Bean
        public ChromaApi chromaApi() {
            String chromaDbHost = chromadb.getHost();
            int chromaDbPort = chromadb.getMappedPort(8000);  // Adjust port if necessary

            String baseUrl = String.format("http://%s:%d", chromaDbHost, chromaDbPort);
            return new ChromaApi(baseUrl);
        }

        record Request(String text) {
        }

        record Response(String text) {
        }
    }
}
