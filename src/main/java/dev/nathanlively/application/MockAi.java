package dev.nathanlively.application;

import dev.nathanlively.adapter.in.web.droidcomm.UserMessageDto;
import dev.nathanlively.application.port.AiGateway;
import reactor.core.publisher.Flux;

public class MockAi implements AiGateway {
    @Override
    public Flux<String> sendMessageAndReceiveReplies(UserMessageDto userMessageDto) {
        // Return different responses based on the input message
        if ("comm check".equalsIgnoreCase(userMessageDto.userMessageText())) {
            return Flux.just("good check");
        } else if ("weather".equalsIgnoreCase(userMessageDto.userMessageText())) {
            return Flux.just("The weather is sunny");
        } else if ("clock in to project A".equalsIgnoreCase(userMessageDto.userMessageText())) {
            // Simulate a JSON response indicating function call
            String jsonResponse = "{\"function_call\": {\"name\": \"clockOut\", \"arguments\": {\"projectId\": \"A\"}}}";
            return Flux.just(jsonResponse);

        } else {
            return Flux.just("Hello!");
        }
    }
}
