package dev.nathanlively.application;

import java.util.function.Function;

public class ClockInServiceAi implements Function<ClockInRequest, ClockInResponse> {
    private final ClockInService clockInService;

    public ClockInServiceAi(ClockInService clockInService) {
        this.clockInService = clockInService;
    }

    @Override
    public ClockInResponse apply(ClockInRequest clockInRequest) {
        return clockInService.clockIn(clockInRequest);
    }
}
