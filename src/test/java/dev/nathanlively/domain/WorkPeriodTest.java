package dev.nathanlively.domain;

import dev.nathanlively.domain.exceptions.InvalidClockOutTimeException;
import dev.nathanlively.domain.exceptions.InvalidWorkPeriodException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkPeriodTest {
    @Test
    void newWorkPeriod_givenEndProceedsStart_throws() {
        assertThatThrownBy(() -> new WorkPeriod(Instant.now(), Instant.now().minusSeconds(3600)))
                .isInstanceOf(InvalidClockOutTimeException.class)
                .hasMessage("End time cannot be before start time.");
    }

    @Test
    void setEnd_givenEndProceedsStart_throws() {
        WorkPeriod workPeriod = WorkPeriod.startAt(Instant.now());
        assertThatThrownBy(() -> workPeriod.setEnd(Instant.now().minusSeconds(3600)))
                .isInstanceOf(InvalidClockOutTimeException.class)
                .hasMessage("End time cannot be before start time.");
    }

    @Test
    void newWorkPeriod_givenStartTimeInFuture_throws() {
        assertThatThrownBy(() -> new WorkPeriod(Instant.now().plusSeconds(3600), Instant.now().plusSeconds(7200)))
                .isInstanceOf(InvalidWorkPeriodException.class)
                .hasMessage("Start time cannot be in the future.");
    }

    @Test
    void newWorkPeriod_givenEndTimeInFuture_throws() {
        assertThatThrownBy(() -> new WorkPeriod(Instant.now(), Instant.now().plusSeconds(3600)))
                .isInstanceOf(InvalidWorkPeriodException.class)
                .hasMessage("End time cannot be in the future.");
    }

    @Test
    void setEnd_givenEndTimeInFuture_throws() {
        WorkPeriod workPeriod = WorkPeriod.startAt(Instant.now());
        assertThatThrownBy(() -> workPeriod.setEnd(Instant.now().plusSeconds(3600)))
                .isInstanceOf(InvalidWorkPeriodException.class)
                .hasMessage("End time cannot be in the future.");
    }

    static Stream<Arguments> provideOverlappingPeriods() {
        // Initialize common instances for testing
        Instant now = Instant.now();
        Instant oneHourAgo = now.minusSeconds(60 * 60);
        Instant thirtyMinutesAgo = now.minusSeconds(60 * 30);
        Instant twentyNineMinutesAgo = now.minusSeconds(60 * 29);
        Instant tenMinutesAgo = now.minusSeconds(60 * 10);

        Instant firstEntryStart = LocalDateTime.of(2024, 3, 15, 9, 0).atZone(ZoneId.of("America/Chicago")).toInstant();
        Instant firstEntryEnd = LocalDateTime.of(2024, 3, 15, 10, 0).atZone(ZoneId.of("America/Chicago")).toInstant();
        Instant secondEntryEnd = firstEntryEnd.plus(Duration.ofMinutes(20));

        return Stream.of(
                // 1. No overlap
                Arguments.of(new WorkPeriod(oneHourAgo, thirtyMinutesAgo),
                        new WorkPeriod(twentyNineMinutesAgo, now),
                        false),

                // 2. No overlap (exact)
                Arguments.of(new WorkPeriod(firstEntryStart, firstEntryEnd),
                        new WorkPeriod(firstEntryEnd, secondEntryEnd),
                        false),

                // 3. No overlap (exact)
                Arguments.of(new WorkPeriod(oneHourAgo, thirtyMinutesAgo),
                        new WorkPeriod(thirtyMinutesAgo, now),
                        false),

                // 4. General overlap
                Arguments.of(new WorkPeriod(oneHourAgo, tenMinutesAgo),
                        new WorkPeriod(twentyNineMinutesAgo, now),
                        true),

                // 5. Fully contained
                Arguments.of(new WorkPeriod(oneHourAgo, now),
                        new WorkPeriod(thirtyMinutesAgo, tenMinutesAgo),
                        true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOverlappingPeriods")
    void overlaps(WorkPeriod period1, WorkPeriod period2, boolean expected) throws Exception {
        boolean actual = period1.overlaps(period2);
        assertThat(actual).isEqualTo(expected);
    }
}