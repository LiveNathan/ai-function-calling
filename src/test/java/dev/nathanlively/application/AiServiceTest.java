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
        Flux<String> expected = Flux.just("good check");

        Flux<String> actual = service.sendMessageAndReceiveReplies("comm check", null);

        StepVerifier.create(actual)
                .expectNext("good check")
                .verifyComplete();
    }
}