package dev.nathanlively.application;

import dev.nathanlively.domain.WorkPeriod;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class WorkPeriodDtoTest {
    @Test
    void fromDomain() throws Exception {
        Instant now = Instant.now();
        WorkPeriod workPeriod = WorkPeriod.startAt(now);
        WorkPeriodDto expected = new WorkPeriodDto(now, null, null);

        WorkPeriodDto actual = WorkPeriodDto.from(workPeriod);

        assertThat(actual)
                .isEqualTo(expected);
    }

}