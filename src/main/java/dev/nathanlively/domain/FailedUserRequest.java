package dev.nathanlively.domain;

import java.time.Instant;
import java.util.Objects;

public class FailedUserRequest {
    private final String email;
    private final String request;
    private final String reasonForFailure;
    private final Instant timestamp;
    private RequestCategory category;

    public FailedUserRequest(String email, String request, String reasonForFailure, Instant timestamp, RequestCategory category) {
        this.email = validateEmail(email);
        this.request = validateRequest(request);
        this.reasonForFailure = validateReason(reasonForFailure);
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp must not be null");
        this.category = Objects.requireNonNull(category, "Category must not be null");
    }

    private String validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or blank");
        }
        return email;
    }

    private String validateRequest(String request) {
        if (request == null || request.isBlank()) {
            throw new IllegalArgumentException("Request must not be null or blank");
        }
        return request;
    }

    private String validateReason(String reasonForFailure) {
        if (reasonForFailure == null || reasonForFailure.isBlank()) {
            throw new IllegalArgumentException("Reason for failure must not be null or blank");
        }
        return reasonForFailure;
    }
}
