package dev.nathanlively.application;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class InputValidator {
    public static List<String> validateInputs(String resourceEmail, String projectName, String zone) {
        List<String> errors = new ArrayList<>();
        if (projectName == null || projectName.trim().isEmpty()) {
            errors.add("Project name must not be null or empty.");
        }
        if (zone == null || zone.trim().isEmpty()) {
            errors.add("Zone must not be null or empty.");
        }
        if (resourceEmail == null || resourceEmail.trim().isEmpty()) {
            errors.add("Email must not be null or empty.");
        }
        return errors;
    }

    public static List<String> validateInputs(String resourceEmail, String projectName, String zone, Duration duration) {
        List<String> errors = validateInputs(resourceEmail, projectName, zone);
        if (duration == null || duration.isZero()) {
            errors.add("Duration must not be null or zero.");
        }
        return errors;
    }
}
