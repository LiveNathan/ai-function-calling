package dev.nathanlively.application;

import dev.nathanlively.domain.UnfulfilledUserRequest;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UnfulfilledRequestService {
    private final VectorStore vectorStore;

    public UnfulfilledRequestService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public Set<UnfulfilledUserRequest> findAll() {
        List<Document> documents = fetchDocuments();
        return documents.stream()
                .map(this::createUserRequest)
                .filter(request -> request.conversationId() != null)
                .collect(Collectors.toSet());
    }

    private List<Document> fetchDocuments() {
        return vectorStore.similaritySearch(SearchRequest.query("I don't have the capability")
                .withTopK(10)
                .withSimilarityThreshold(0.6));
    }

    private UnfulfilledUserRequest createUserRequest(Document document) {
        String content = document.getContent();
        String conversationId = extractConversationId(document.getMetadata());
        return new UnfulfilledUserRequest(content, conversationId);
    }

    private String extractConversationId(Map<String, Object> metadata) {
        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("conversationId")) {
                return entry.getValue().toString();
            }
        }
        return null;
    }

}
