package dev.nathanlively.application;

import dev.nathanlively.domain.WorkPeriod;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class WorkPeriodDtoTest {
    @Test
    void fromDomain() throws Exception {
        Instant start = LocalDateTime.of(2024, 3, 15, 9, 0).atZone(ZoneId.of("America/Chicago")).toInstant();
        Instant end = LocalDateTime.of(2024, 3, 15, 10, 0).atZone(ZoneId.of("America/Chicago")).toInstant();
        WorkPeriod workPeriod = new WorkPeriod(start, end);
        WorkPeriodDto expected = new WorkPeriodDto(start, end, Duration.ofHours(1));

        WorkPeriodDto actual = WorkPeriodDto.from(workPeriod);

        assertThat(actual)
                .isEqualTo(expected);
    }

}