package dev.nathanlively.application;

import dev.nathanlively.application.port.AiGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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
        Flux<String> actual = service.sendMessageAndReceiveReplies("comm check", null);

        StepVerifier.create(actual)
                .expectNext("good check")
                .verifyComplete();
    }

    @Test
    void sendMessageAndReceiveReplies_clockIn_returnsJson() {
        String jsonResponse = "{\"function_call\": {\"name\": \"clockIn\", \"arguments\": {\"projectId\": \"A\"}}}";

        Flux<String> actual = service.sendMessageAndReceiveReplies("clock in to project A", null);

        StepVerifier.create(actual)
                .expectNext(jsonResponse)
                .verifyComplete();
    }
}