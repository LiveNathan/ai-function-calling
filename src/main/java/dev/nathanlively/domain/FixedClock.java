package dev.nathanlively.domain;

import java.time.Instant;
import java.util.Objects;

public class FixedClock implements MyClock {
    private final Instant fixedInstant;

    public FixedClock(Instant fixedInstant) {
        this.fixedInstant = Objects.requireNonNull(fixedInstant);
    }

    @Override
    public Instant now() {
        return fixedInstant;
    }
}
