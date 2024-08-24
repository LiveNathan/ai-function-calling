package dev.nathanlively.application.port;
import reactor.core.publisher.Flux;

public interface AiGateway {
    Flux<String> sendMessageAndReceiveReplies(String message, String chatId);
}
