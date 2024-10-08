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
import dev.nathanlively.application.functions.createtimesheetentrywithduration.CreateTimesheetEntryWithDurationFunction;
import dev.nathanlively.application.functions.createtimesheetentrywithduration.CreateTimesheetEntryWithDurationRequest;
import dev.nathanlively.application.functions.createtimesheetentrywithduration.CreateTimesheetEntryWithDurationResponse;
import dev.nathanlively.application.functions.findallprojectnames.FindAllProjectNamesFunction;
import dev.nathanlively.application.functions.findallprojectnames.FindAllProjectNamesRequest;
import dev.nathanlively.application.functions.findallprojectnames.FindAllProjectNamesResponse;
import dev.nathanlively.application.functions.getrecenttimesheetentry.GetRecentTimesheetEntryFunction;
import dev.nathanlively.application.functions.getrecenttimesheetentry.GetRecentTimesheetEntryRequest;
import dev.nathanlively.application.functions.getrecenttimesheetentry.GetRecentTimesheetEntryResponse;
import dev.nathanlively.application.functions.updateprojecthours.UpdateProjectHoursFunction;
import dev.nathanlively.application.functions.updateprojecthours.UpdateProjectHoursRequest;
import dev.nathanlively.application.functions.updateprojecthours.UpdateProjectHoursResponse;
import dev.nathanlively.application.port.ProjectRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionConfig {
    @Bean
    @Description("Create a new project.")
    public Function<CreateProjectRequest, CreateProjectResponse> createProject(CreateProjectService service) {
        return new CreateProjectFunction(service);
    }

    @Bean
    @Description("Create a new timesheet entry for a resource when the end time is unknown. (eg. 'I'm starting work on Project A now.')")
    public Function<ClockInRequest, ClockInResponse> clockIn(ClockInService service) {
        return new ClockInFunction(service);
    }

    @Bean
    @Description("Update the end time of an existing timesheet entry for a resource. (eg. 'Stop work on Project A.')")
    public Function<ClockOutRequest, ClockOutResponse> clockOut(ClockOutService service) {
        return new ClockOutFunction(service);
    }

    @Bean
    @Description("Create a new timesheet entry for a resource. Use this when the start and end time are known. (eg. 'I worked on Project A 8-9am.')")
    public Function<CreateTimesheetEntryRequest, CreateTimesheetEntryResponse> createTimesheetEntry(CreateTimesheetEntryService service) {
        return new CreateTimesheetEntryFunction(service);
    }

    @Bean
    @Description("Create a new timesheet entry for a resource. Use this when the duration is known. (eg. 'I worked on Project A for 30 minutes.')")
    public Function<CreateTimesheetEntryWithDurationRequest, CreateTimesheetEntryWithDurationResponse> createTimesheetEntryWithDuration(CreateTimesheetEntryService service) {
        return new CreateTimesheetEntryWithDurationFunction(service);
    }

    @Bean
    @Description("Get the most recent timesheet entry for a resource. Use this when the user asks about recent activity. (eg. 'What am I currently clocked into?')")
    public Function<GetRecentTimesheetEntryRequest, GetRecentTimesheetEntryResponse> getMostRecentTimesheetEntry(GetRecentTimesheetEntryService service) {
        return new GetRecentTimesheetEntryFunction(service);
    }

    @Bean
    @Description("Fetch the list of available project names.")
    public Function<FindAllProjectNamesRequest, FindAllProjectNamesResponse> findAllProjectNames(ProjectRepository repository) {
        return new FindAllProjectNamesFunction(repository);
    }

    @Bean
    @Description("Update the estimated labor hours of a project. (eg. 'Project A should have 100 hours of labor.'")
    public Function<UpdateProjectHoursRequest, UpdateProjectHoursResponse> updateProjectHours(UpdateProjectHoursService service) {
        return new UpdateProjectHoursFunction(service);
    }

}
