package dev.nathanlively.domain.exceptions;

public class NoTimesheetEntriesException extends TimesheetException{
    public NoTimesheetEntriesException() {
        super("No timesheet entries found.");
    }
}
