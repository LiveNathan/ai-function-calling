package dev.nathanlively.application.functions.clockin;

import dev.nathanlively.application.ClockInService;

import java.util.function.Function;

public class ClockInFunction implements Function<ClockInRequest, ClockInResponse> {
    private final ClockInService clockInService;

    public ClockInFunction(ClockInService clockInService) {
        this.clockInService = clockInService;
    }

    @Override
    public ClockInResponse apply(ClockInRequest clockInRequest) {
        return clockInService.clockIn(clockInRequest);
    }
}
