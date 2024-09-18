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
        ZoneId zoneId = ZoneId.of("America/Chicago");
        LocalDateTime localDateTimeStart = LocalDateTime.of(2024, 3, 15, 9, 0);
        LocalDateTime localDateTimeEnd = LocalDateTime.of(2024, 3, 15, 10, 0);
        Instant start = localDateTimeStart.atZone(zoneId).toInstant();
        Instant end = localDateTimeEnd.atZone(zoneId).toInstant();
        WorkPeriod workPeriod = new WorkPeriod(start, end);
        WorkPeriodDto expected = new WorkPeriodDto(localDateTimeStart, localDateTimeEnd, Duration.ofHours(1));

        WorkPeriodDto actual = WorkPeriodDto.from(workPeriod, zoneId);

        assertThat(actual)
                .isEqualTo(expected);
    }

}