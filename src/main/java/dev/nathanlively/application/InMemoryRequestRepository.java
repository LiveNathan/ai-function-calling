package dev.nathanlively.application;

import dev.nathanlively.application.port.RequestRepository;
import dev.nathanlively.domain.UnfulfilledUserRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRequestRepository implements RequestRepository {
    private final Map<String, UnfulfilledUserRequest> requests;

    public InMemoryRequestRepository(Map<String, UnfulfilledUserRequest> requests) {
        this.requests = requests;
    }

    public static InMemoryRequestRepository create(Map<String, UnfulfilledUserRequest> failedRequestsByEmail) {
        return new InMemoryRequestRepository(failedRequestsByEmail);
    }

    public static RequestRepository createEmpty(){
        return create(new HashMap<>());
    }

    @Override
    public String save(UnfulfilledUserRequest unfulfilledUserRequest) {
        if (unfulfilledUserRequest == null ||
                unfulfilledUserRequest.email() == null ||
                unfulfilledUserRequest.email().isBlank()) {
            return "FailedUserRequest or email must not be null or blank";
        }

        requests.put(unfulfilledUserRequest.email(), unfulfilledUserRequest);
        return "The system administrator will be notified. Successfully stored one unfulfilledRequest: " + unfulfilledUserRequest;
    }

    @Override
    public void saveAll(List<UnfulfilledUserRequest> unfulfilledUserRequests) {
        for (UnfulfilledUserRequest unfulfilledUserRequest : unfulfilledUserRequests) {
            save(unfulfilledUserRequest);
        }
    }
}
