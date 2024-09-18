package dev.nathanlively.application;

import dev.nathanlively.adapter.in.web.droidcomm.UserMessageDto;
import dev.nathanlively.application.port.AiGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.ZoneId;

class AiServiceTest {
    AiGateway gateway;
    private AiService service;

    @BeforeEach
    void setUp() {
        gateway = new MockAi();
        service = new AiService(gateway);
    }

    @Test
    void sendMessageAndReceiveReplies_commCheck() {
        UserMessageDto userMessageDto = new UserMessageDto(Instant.now(), "Nathan", "comm check", "chatId1", ZoneId.of("America/Chicago").toString(), "nathanlively@gmail.com");
        Flux<String> actual = service.sendMessageAndReceiveReplies(userMessageDto);

        StepVerifier.create(actual)
                .expectNext("good check")
                .verifyComplete();
    }

    @Test
    void sendMessageAndReceiveReplies_clockIn_returnsJson() {
        UserMessageDto userMessageDto = new UserMessageDto(Instant.now(), "Nathan", "clock in to project A", "chatId1", ZoneId.of("America/Chicago").toString(), "nathanlively@gmail.com");
        String jsonResponse = "{\"function_call\": {\"getName\": \"clockOut\", \"arguments\": {\"projectId\": \"A\"}}}";

        Flux<String> actual = service.sendMessageAndReceiveReplies(userMessageDto);

        StepVerifier.create(actual)
                .expectNext(jsonResponse)
                .verifyComplete();
    }
}