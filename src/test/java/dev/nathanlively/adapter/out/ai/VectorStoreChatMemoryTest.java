package dev.nathanlively.adapter.out.ai;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chroma.ChromaApi;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.ai.vectorstore.ChromaVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.testcontainers.chromadb.ChromaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("database")
@Testcontainers
public class VectorStoreChatMemoryTest {

    @Container
    static ChromaDBContainer chromaContainer = new ChromaDBContainer("chromadb/chroma:0.4.23");

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestApplication.class)
            .withPropertyValues("spring.ai.openai.apiKey=" + System.getenv("OPENAI_API_KEY"));

    List<Document> documents = List.of(
            new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!",
                    Collections.singletonMap("meta1", "meta1")),
            new Document("Hello World Hello World Hello World Hello World Hello World Hello World Hello World"),
            new Document("Great Depression Great Depression Great Depression Great Depression Great Depression Great Depression",
                    Collections.singletonMap("meta2", "meta2")));

    @Test
    public void addAndSearch() {
        contextRunner.run(context -> {

            VectorStore vectorStore = context.getBean(VectorStore.class);

            vectorStore.add(documents);

            List<Document> results = vectorStore.similaritySearch(SearchRequest.query("Great").withTopK(1));

            assertThat(results).hasSize(1);
            Document resultDoc = results.getFirst();
            assertThat(resultDoc.getId()).isEqualTo(documents.get(2).getId());
            assertThat(resultDoc.getContent()).isEqualTo(
                    "Great Depression Great Depression Great Depression Great Depression Great Depression Great Depression");
            assertThat(resultDoc.getMetadata()).containsKeys("meta2", "distance");

            // Remove all documents from the store
            vectorStore.delete(documents.stream().map(Document::getId).toList());

            List<Document> results2 = vectorStore.similaritySearch(SearchRequest.query("Great").withTopK(1));
            assertThat(results2).hasSize(0);
        });
    }

    @SpringBootConfiguration
    public static class TestApplication {

        @Bean
        public FunctionCallbackContext springAiFunctionManager() {
            return new FunctionCallbackContext();
        }

        @Bean
        public OpenAiApi openAiApi() {
            String apiKey = System.getenv("OPENAI_API_KEY");
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalArgumentException("You must provide an API key. Set it in the environment variable 'OPENAI_API_KEY'");
            }
            return new OpenAiApi(apiKey);
        }

        @Bean
        public ChatModel openAiChatModel(OpenAiApi api, FunctionCallbackContext functionCallbackContext) {
            return new OpenAiChatModel(api, OpenAiChatOptions.builder().withModel(OpenAiApi.ChatModel.GPT_4_O_MINI).build(), functionCallbackContext, RetryUtils.DEFAULT_RETRY_TEMPLATE);
        }

        @Bean
        public EmbeddingModel openAiEmbeddingModel(OpenAiApi api) {
            return new OpenAiEmbeddingModel(api);
        }

        @Bean
        public ChromaApi chromaApi() {
            return new ChromaApi(chromaContainer.getEndpoint());
        }

        @Bean
        public VectorStore chromaVectorStore(EmbeddingModel embeddingModel, ChromaApi chromaApi) {
            return new ChromaVectorStore(embeddingModel, chromaApi, true);
        }

    }
}
