package dev.nathanlively.domain;

public class InvalidClockOutTimeException extends IllegalArgumentException {
    public InvalidClockOutTimeException(String message) {
        super(message);
    }
}
