package dev.nathanlively.domain.exceptions;

public class TimesheetException extends RuntimeException{
    public TimesheetException(String message) {
        super(message);
    }
}