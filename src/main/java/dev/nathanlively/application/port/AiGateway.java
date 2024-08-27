package dev.nathanlively.application.port;
import dev.nathanlively.adapter.in.web.droidcomm.UserMessageDto;
import reactor.core.publisher.Flux;

public interface AiGateway {
    Flux<String> sendMessageAndReceiveReplies(UserMessageDto userMessageDto);
}
