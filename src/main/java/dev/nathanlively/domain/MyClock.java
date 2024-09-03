package dev.nathanlively.domain;

import java.time.Instant;

public interface MyClock {
    Instant now();
}
