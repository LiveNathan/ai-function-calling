package dev.nathanlively.application;

import dev.nathanlively.application.port.RequestRepository;
import dev.nathanlively.domain.UnfulfilledUserRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InMemoryRequestRepository implements RequestRepository {
    private final Set<UnfulfilledUserRequest> requests;

    public InMemoryRequestRepository(Set<UnfulfilledUserRequest> requests) {
        this.requests = requests;
    }

    public static InMemoryRequestRepository create(Set<UnfulfilledUserRequest> failedRequests) {
        return new InMemoryRequestRepository(failedRequests);
    }

    public static RequestRepository createEmpty() {
        return create(new HashSet<>());
    }

    @Override
    public String save(UnfulfilledUserRequest unfulfilledUserRequest) {
        requests.add(unfulfilledUserRequest);
        return "The system administrator will be notified. Successfully stored one unfulfilledRequest: " + unfulfilledUserRequest;
    }

    @Override
    public void saveAll(List<UnfulfilledUserRequest> unfulfilledUserRequests) {
        for (UnfulfilledUserRequest unfulfilledUserRequest : unfulfilledUserRequests) {
            save(unfulfilledUserRequest);
        }
    }
}
