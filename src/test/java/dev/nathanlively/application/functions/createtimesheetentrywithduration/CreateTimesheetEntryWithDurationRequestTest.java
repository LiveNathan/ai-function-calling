package dev.nathanlively.application.functions.createtimesheetentrywithduration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class CreateTimesheetEntryWithDurationRequestTest {
    @Test
    void mapToObject() throws Exception {
        String json = """
                {
                    "projectName": "Project A",
                    "timesheetEntryDuration": "PT30M",
                    "zoneId": "America/Chicago"
                }
                """;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        CreateTimesheetEntryWithDurationRequest actual = objectMapper.readValue(json, new TypeReference<>() {
        });

        CreateTimesheetEntryWithDurationRequest expected = new CreateTimesheetEntryWithDurationRequest(
                "Project A", Duration.ofMinutes(30), "America/Chicago");

        assertThat(actual)
                .isEqualTo(expected);
    }

}