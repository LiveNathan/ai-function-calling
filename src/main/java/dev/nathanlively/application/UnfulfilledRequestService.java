package dev.nathanlively.application;

import dev.nathanlively.application.port.RequestRepository;
import dev.nathanlively.domain.UnfulfilledUserRequest;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UnfulfilledRequestService {
    private final VectorStore vectorStore;
    private final RequestRepository requestRepository;

    public UnfulfilledRequestService(VectorStore vectorStore, RequestRepository requestRepository) {
        this.vectorStore = vectorStore;
        this.requestRepository = requestRepository;
    }

    public List<UnfulfilledUserRequest> findAll() {
        List<Document> documents = fetchDocuments();
        List<UnfulfilledUserRequest> unfulfilledUserRequests = documents.stream()
                .map(this::createUserRequest)
                .filter(request -> request.conversationId() != null)
                .collect(Collectors.toList());
        saveRequests(unfulfilledUserRequests);
        return unfulfilledUserRequests;
    }

    private List<Document> fetchDocuments() {
        return vectorStore.similaritySearch(SearchRequest.query("I don't have the capability").withTopK(10));
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

    private void saveRequests(List<UnfulfilledUserRequest> unfulfilledUserRequests) {
        requestRepository.saveAll(unfulfilledUserRequests);
    }
}
