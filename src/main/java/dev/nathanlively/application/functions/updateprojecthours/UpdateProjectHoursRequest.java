package dev.nathanlively.application.functions.updateprojecthours;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record UpdateProjectHoursRequest(
        @JsonProperty(required = true) @JsonPropertyDescription("Project getName must match one of the currently available projects. If it doesn't, you can offer to create it.") String projectName,
        @JsonProperty(required = true) @JsonPropertyDescription("Integer greater than zero.") int estimatedHours
) {
}
