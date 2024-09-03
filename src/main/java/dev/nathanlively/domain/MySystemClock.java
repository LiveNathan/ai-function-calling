package dev.nathanlively.domain;

import java.time.Instant;

public class MySystemClock implements MyClock {
    @Override
    public Instant now() {
        return Instant.now();
    }
}
