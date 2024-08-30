package dev.nathanlively.application;

import dev.nathanlively.application.port.RequestRepository;
import dev.nathanlively.domain.RequestCategory;
import dev.nathanlively.domain.UnfulfilledUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UnfulfilledRequestService {
    private static final Logger log = LoggerFactory.getLogger(UnfulfilledRequestService.class);
    private final VectorStore vectorStore;
    private final RequestRepository requestRepository;

    public UnfulfilledRequestService(VectorStore vectorStore, RequestRepository requestRepository) {
        this.vectorStore = vectorStore;
        this.requestRepository = requestRepository;
    }

    public List<UnfulfilledUserRequest> findAll() {
        List<UnfulfilledUserRequest> unfulfilledUserRequests = new ArrayList<>();
        List<Document> documents = vectorStore.similaritySearch("I don't have the capability");
        for (Document document : documents) {
            String content = document.getContent();
            document.getMetadata().forEach((key, value) -> {
                log.info("Metadata - Key: {}, Value: {}", key, value);
            });

            UnfulfilledUserRequest unfulfilledUserRequest = new UnfulfilledUserRequest("nathanlively@gmail.com",
                    content, "unsure", Instant.now(), RequestCategory.OTHER);
            unfulfilledUserRequests.add(unfulfilledUserRequest);
        }
        requestRepository.saveAll(unfulfilledUserRequests);
        return unfulfilledUserRequests;
    }
}
