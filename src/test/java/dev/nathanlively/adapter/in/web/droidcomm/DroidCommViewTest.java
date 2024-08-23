package dev.nathanlively.adapter.in.web.droidcomm;

import dev.nathanlively.application.AiService;
import dev.nathanlively.application.InMemoryAi;
import dev.nathanlively.application.port.AiGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("ui")
class DroidCommViewTest {
    private DroidCommView view;

    @BeforeEach
    void setUp() {
        AiGateway aiGateway = new InMemoryAi();
        AiService aiService = new AiService(aiGateway);
        view = new DroidCommView(aiService);
    }
    
    @Test
    void getRequestToIndex_returnsView() {
        assertThat(view).isNotNull();
    }
}