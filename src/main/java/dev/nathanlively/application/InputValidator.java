package dev.nathanlively.application;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class InputValidator {

    private InputValidator() {
        // private constructor to prevent instantiation
    }

    public static List<String> validateInputs(String resourceEmail, String projectName, String zone) {
        List<String> errors = new ArrayList<>();
        validateString(errors, projectName);
        validateZone(errors, zone);
        validateResourceEmail(errors, resourceEmail);
        return errors;
    }

    public static List<String> validateInputs(String resourceEmail, String projectName, String zone, Duration duration) {
        List<String> errors = validateInputs(resourceEmail, projectName, zone);
        validateDuration(errors, duration);
        return errors;
    }

    public static List<String> validateInputs(String projectName, int estimatedHours) {
        List<String> errors = new ArrayList<>();
        validateString(errors, projectName);
        validateEstimatedHours(errors, estimatedHours);
        return errors;
    }

    private static void validateString(List<String> errors, String string) {
        if (string == null || string.trim().isEmpty()) {
            errors.add("Project name must not be null or empty.");
        }
    }

    private static void validateZone(List<String> errors, String zone) {
        if (zone == null || zone.trim().isEmpty()) {
            errors.add("Zone must not be null or empty.");
        }
    }

    private static void validateResourceEmail(List<String> errors, String resourceEmail) {
        if (resourceEmail == null || resourceEmail.trim().isEmpty()) {
            errors.add("Email must not be null or empty.");
        }
    }

    private static void validateDuration(List<String> errors, Duration duration) {
        if (duration == null || duration.isZero()) {
            errors.add("Duration must not be null or zero.");
        }
    }

    private static void validateEstimatedHours(List<String> errors, int estimatedHours) {
        if (estimatedHours <= 0) {
            errors.add("Estimated hours must be greater than zero.");
        }
    }

    public static List<String> validateInputs(String... strings) {
        List<String> errors = new ArrayList<>();
        for (String string : strings) {
            validateString(errors, string);
        }
        return errors;
    }
}
