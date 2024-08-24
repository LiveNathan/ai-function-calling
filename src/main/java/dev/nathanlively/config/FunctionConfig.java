package dev.nathanlively.config;

import dev.nathanlively.application.MockWeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionConfig {
    @Bean
    @Description("Get the weather in location") // function description
    public Function<MockWeatherService.Request, MockWeatherService.Response> weatherFunction1() {
//        return new MockWeatherService();
        return null;
    }
}
