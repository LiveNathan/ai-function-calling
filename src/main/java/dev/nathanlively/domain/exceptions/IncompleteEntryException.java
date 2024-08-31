package dev.nathanlively.domain.exceptions;

public class IncompleteEntryException extends TimesheetException{
    public IncompleteEntryException() {
        super("Cannot clock in. The previous entry has not been clocked out.");
    }
}
