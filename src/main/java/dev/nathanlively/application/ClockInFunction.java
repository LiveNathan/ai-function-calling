package dev.nathanlively.application;

import dev.nathanlively.application.clockin.ClockInRequest;
import dev.nathanlively.application.clockin.ClockInResponse;

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
