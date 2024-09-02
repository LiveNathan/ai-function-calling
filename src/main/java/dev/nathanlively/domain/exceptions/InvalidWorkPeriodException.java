package dev.nathanlively.domain.exceptions;

public class InvalidWorkPeriodException extends RuntimeException {
    public InvalidWorkPeriodException(String message) {
        super(message);
    }
}
