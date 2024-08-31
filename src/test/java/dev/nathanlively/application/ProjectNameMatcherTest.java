package dev.nathanlively.application;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectNameMatcherTest {
    @Test
    void from() throws Exception {
        ProjectNameMatcher service = new ProjectNameMatcher();
        String expected = "Project A (12345)";

        String actual = service.from("projct a");

        assertThat(actual)
                .isEqualTo(expected);
    }
}