package dev.nathanlively.adapter.out.ai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import static org.assertj.core.api.Assertions.assertThat;

class SpringAiAdapterTest {
    //    @Value("classpath:prompt.st")
//    @Value("classpath:src/test/resources/prompt.st")
    @Value("classpath:/Users/nathanlively/Documents/Sync/Websites/demos/ai-function-calling/src/test/resources/prompt.st")
    private Resource prompt;

    @Test
    void promptLoadedFromFile() throws Exception {
        assertThat(prompt.exists()).isTrue();
    }
}