package dev.nathanlively.domain;

import jakarta.validation.constraints.NotBlank;

import static dev.nathanlively.common.validation.Validation.validate;


public record UnfulfilledUserRequest(
        @NotBlank(message = "Request must not be null or blank")
        String unfulfilledRequest,
        @NotBlank
        String conversationId) {
        public UnfulfilledUserRequest(@NotBlank(message = "Request must not be null or blank") String unfulfilledRequest,
                                      @NotBlank String conversationId) {
                this.unfulfilledRequest = unfulfilledRequest;
                this.conversationId = conversationId;
                validate(this);
        }
}
