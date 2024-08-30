package dev.nathanlively.application.port;

import dev.nathanlively.domain.UnfulfilledUserRequest;

import java.util.List;

public interface RequestRepository {
    String save(UnfulfilledUserRequest unfulfilledUserRequest);

    void saveAll(List<UnfulfilledUserRequest> unfulfilledUserRequests);
}
