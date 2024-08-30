package dev.nathanlively.application;

import dev.nathanlively.application.port.RequestRepository;
import dev.nathanlively.domain.UnfulfilledUserRequest;

import java.util.ArrayList;
import java.util.List;

public class InMemoryRequestRepository implements RequestRepository {
    private final List<UnfulfilledUserRequest> requests;

    public InMemoryRequestRepository(List<UnfulfilledUserRequest> requests) {
        this.requests = requests;
    }

    public static InMemoryRequestRepository create(List<UnfulfilledUserRequest> failedRequests) {
        return new InMemoryRequestRepository(failedRequests);
    }

    public static RequestRepository createEmpty() {
        return create(new ArrayList<>());
    }

    @Override
    public String save(UnfulfilledUserRequest unfulfilledUserRequest) {
        requests.add(unfulfilledUserRequest);
        return "The system administrator will be notified. Successfully stored one unfulfilledRequest: " + unfulfilledUserRequest;
    }
}
