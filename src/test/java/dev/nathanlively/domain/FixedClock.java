package dev.nathanlively.domain;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class FixedClock extends Clock {
    private final ZoneId zone;
    private Instant fixedInstant;

    public FixedClock(Instant fixedInstant, ZoneId zone) {
        this.fixedInstant = fixedInstant;
        this.zone = zone;
    }

    public void setFixedInstant(Instant fixedInstant) {
        this.fixedInstant = fixedInstant;
    }

    public void setToDefaultTestTime() {
        this.fixedInstant = Instant.parse("2023-10-10T10:00:00Z");
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new FixedClock(fixedInstant, zone);
    }

    @Override
    public Instant instant() {
        return fixedInstant;
    }
}
