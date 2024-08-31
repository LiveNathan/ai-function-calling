package dev.nathanlively.application.functions.clockout;

import dev.nathanlively.application.ClockOutService;

import java.util.function.Function;

public class ClockOutFunction implements Function<ClockOutRequest, ClockOutResponse> {
    private final ClockOutService clockOutService;

    public ClockOutFunction(ClockOutService clockOutService) {
        this.clockOutService = clockOutService;
    }

    @Override
    public ClockOutResponse apply(ClockOutRequest clockOutRequest) {
        return clockOutService.clockOut(clockOutRequest);
    }
}
