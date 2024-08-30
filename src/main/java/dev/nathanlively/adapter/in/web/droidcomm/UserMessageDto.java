package dev.nathanlively.adapter.in.web.droidcomm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UserMessageDto(@NotNull Instant creationTime, @NotBlank String userName, @NotBlank String userMessageText,
                             @NotBlank String chatId) {
}
