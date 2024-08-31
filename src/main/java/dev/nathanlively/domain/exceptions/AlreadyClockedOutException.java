package dev.nathanlively.domain.exceptions;

public class AlreadyClockedOutException extends TimesheetException{
    public AlreadyClockedOutException() {
        super("Cannot clock out. The most recent entry is already clocked out.");
    }
}
