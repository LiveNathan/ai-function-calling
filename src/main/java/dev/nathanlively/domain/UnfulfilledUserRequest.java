package dev.nathanlively.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

import static dev.nathanlively.common.validation.Validation.validate;


public record UnfulfilledUserRequest(

        @NotBlank(message = "Request must not be null or blank")
        String unfulfilledRequest,

        @NotBlank(message = "Reason for failure must not be null or blank")
        String reasonForFailure,

        @NotNull(message = "Timestamp must not be null")
        Instant timestamp,

        @NotNull(message = "Category must not be null")
        RequestCategory category
) {
    public UnfulfilledUserRequest(@NotBlank(message = "Request must not be null or blank")
                             String unfulfilledRequest,
                                  @NotBlank(message = "Reason for failure must not be null or blank")
                             String reasonForFailure,
                                  @NotNull(message = "Timestamp must not be null")
                             Instant timestamp,
                                  @NotNull(message = "Category must not be null")
                             RequestCategory category) {
        this.unfulfilledRequest = unfulfilledRequest;
        this.reasonForFailure = reasonForFailure;
        this.timestamp = timestamp;
        this.category = category;
        validate(this);
    }
}
