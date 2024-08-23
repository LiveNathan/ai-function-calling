package dev.nathanlively.adapter.in.web.droidcomm;

import dev.nathanlively.application.AiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("ui")
class DroidCommViewTest {
    private DroidCommView view;

    @BeforeEach
    void setUp() {
        AiService aiService = new AiService();
        view = new DroidCommView(aiService);
    }
    
    @Test
    void getRequestToIndex_returnsView() {
        assertThat(view).isNotNull();
    }
}