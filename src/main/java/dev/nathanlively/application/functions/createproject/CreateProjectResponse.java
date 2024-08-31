package dev.nathanlively.application.functions.createproject;

import dev.nathanlively.domain.Project;

public record CreateProjectResponse(String message, Project project) {
}
