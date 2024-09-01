package dev.nathanlively.config;

import dev.nathanlively.application.*;
import dev.nathanlively.application.functions.clockin.ClockInFunction;
import dev.nathanlively.application.functions.clockin.ClockInRequest;
import dev.nathanlively.application.functions.clockin.ClockInResponse;
import dev.nathanlively.application.functions.clockout.ClockOutFunction;
import dev.nathanlively.application.functions.clockout.ClockOutRequest;
import dev.nathanlively.application.functions.clockout.ClockOutResponse;
import dev.nathanlively.application.functions.createproject.CreateProjectFunction;
import dev.nathanlively.application.functions.createproject.CreateProjectRequest;
import dev.nathanlively.application.functions.createproject.CreateProjectResponse;
import dev.nathanlively.application.functions.createtimesheetentry.CreateTimesheetEntryFunction;
import dev.nathanlively.application.functions.createtimesheetentry.CreateTimesheetEntryRequest;
import dev.nathanlively.application.functions.createtimesheetentry.CreateTimesheetEntryResponse;
import dev.nathanlively.application.functions.findallprojectnames.FindAllProjectNamesFunction;
import dev.nathanlively.application.functions.findallprojectnames.FindAllProjectNamesRequest;
import dev.nathanlively.application.functions.findallprojectnames.FindAllProjectNamesResponse;
import dev.nathanlively.application.functions.updateproject.UpdateProjectFunction;
import dev.nathanlively.application.functions.updateproject.UpdateProjectRequest;
import dev.nathanlively.application.functions.updateproject.UpdateProjectResponse;
import dev.nathanlively.application.port.ProjectRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionConfig {
    @Bean
    @Description("Create a new project.")
    public Function<CreateProjectRequest, CreateProjectResponse> createProject(CreateProjectService createProjectService) {
        return new CreateProjectFunction(createProjectService);
    }

    @Bean
    @Description("Create a new timesheet entry for a resource when the end time is unknown. The project name is important, but optional. If the project name is known, include it. Otherwise, it can be set to null, and the project can be updated later using the updateProjectFunction. (eg. 'I'm starting work on Project A now.')")
    public Function<ClockInRequest, ClockInResponse> clockIn(ClockInService clockInService) {
        return new ClockInFunction(clockInService);
    }

    @Bean
    @Description("Update the end time of an existing timesheet entry for a resource. (eg. 'Stop work on Project A.')")
    public Function<ClockOutRequest, ClockOutResponse> clockOut(ClockOutService clockOutService) {
        return new ClockOutFunction(clockOutService);
    }

    @Bean
    @Description("Create a new timesheet entry for a resource. Use this when the start and end time are already known. (eg. 'I worked on Project A 8-9am.')")
    public Function<CreateTimesheetEntryRequest, CreateTimesheetEntryResponse> createTimesheetEntry(CreateTimesheetEntryService service) {
        return new CreateTimesheetEntryFunction(service);
    }

    @Bean
    @Description("Update an existing timesheet entry's project. Use this function in the case when a user clocks in without a project and now wants to add the project to it. (eg. 'Never mind. I'm actually working on Project B.')")
    public Function<UpdateProjectRequest, UpdateProjectResponse> updateProject(UpdateTimesheetEntryService updateTimesheetEntryService) {
        return new UpdateProjectFunction(updateTimesheetEntryService);
    }

    @Bean
    @Description("Fetch the list of available project names.")
    public Function<FindAllProjectNamesRequest, FindAllProjectNamesResponse> findAllProjectNames(ProjectRepository repository) {
        return new FindAllProjectNamesFunction(repository);
    }

}
