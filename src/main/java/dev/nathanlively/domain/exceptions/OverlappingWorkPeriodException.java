package dev.nathanlively.domain.exceptions;

public class OverlappingWorkPeriodException extends RuntimeException {
    public OverlappingWorkPeriodException(String message) {
        super(message);
    }
}
