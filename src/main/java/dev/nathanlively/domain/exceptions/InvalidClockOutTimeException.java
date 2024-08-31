package dev.nathanlively.domain.exceptions;

public class InvalidClockOutTimeException extends IllegalArgumentException {
    public InvalidClockOutTimeException(String message) {
        super(message);
    }
}
