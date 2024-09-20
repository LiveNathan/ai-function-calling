package dev.nathanlively.adapter.in.web.droidcomm;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record UserMessageDto(java.time.Instant creationTimeInstant, LocalDateTime creationTime, @NotBlank String userName, @NotBlank String userMessageText,
                             @NotBlank String chatId, String creationTimezone, String email
) {
}
