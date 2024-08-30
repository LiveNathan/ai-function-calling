package dev.nathanlively.application.port;

import dev.nathanlively.domain.UnfulfilledUserRequest;

public interface RequestRepository {
    String save(UnfulfilledUserRequest unfulfilledUserRequest);
}
