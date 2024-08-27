package dev.nathanlively.config;

import dev.nathanlively.application.ClockInRequest;
import dev.nathanlively.application.ClockInResponse;
import dev.nathanlively.application.ClockInService;
import dev.nathanlively.application.ClockInServiceAi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionConfig {
    @Bean
    @Description("Create a new timesheet entry for a resource.")
    public Function<ClockInRequest, ClockInResponse> clockInFunction(ClockInService clockInService) {
        return new ClockInServiceAi(clockInService);
    }
}
