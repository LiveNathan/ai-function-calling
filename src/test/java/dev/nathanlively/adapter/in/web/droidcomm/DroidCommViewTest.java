package dev.nathanlively.adapter.in.web.droidcomm;

import dev.nathanlively.application.AiService;
import dev.nathanlively.application.MockAi;
import dev.nathanlively.application.port.AiGateway;
import dev.nathanlively.security.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("ui")
class DroidCommViewTest {
    private DroidCommView view;
    AuthenticatedUser authenticatedUser;

    @BeforeEach
    void setUp() {
        AiGateway aiGateway = new MockAi();
        AiService aiService = new AiService(aiGateway);
        view = new DroidCommView(aiService, authenticatedUser);
    }
    
    @Test
    void getRequestToIndex_returnsView() {
        assertThat(view).isNotNull();
    }
}