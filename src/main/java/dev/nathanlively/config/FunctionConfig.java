package dev.nathanlively.config;

import dev.nathanlively.application.ClockInFunction;
import dev.nathanlively.application.ClockInService;
import dev.nathanlively.application.UpdateProjectFunction;
import dev.nathanlively.application.clockin.ClockInRequest;
import dev.nathanlively.application.clockin.ClockInResponse;
import dev.nathanlively.application.updateproject.UpdateProjectRequest;
import dev.nathanlively.application.updateproject.UpdateProjectResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionConfig {
    @Bean
    @Description("Create a new timesheet entry for a resource.")
    public Function<ClockInRequest, ClockInResponse> clockInFunction(ClockInService clockInService) {
        return new ClockInFunction(clockInService);
    }

    @Bean
    @Description("Update an existing timesheet entry's project.")
    public Function<UpdateProjectRequest, UpdateProjectResponse> updateProjectFunction(ClockInService clockInService) {
        return new UpdateProjectFunction(clockInService);
    }
}
