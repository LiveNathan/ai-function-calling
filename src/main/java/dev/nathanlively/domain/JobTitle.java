package dev.nathanlively.domain;

public enum JobTitle {
    TECHNICIAN,
    PROGRAMMER,
    OPERATIONS_MANAGER,
    PROJECT_MANAGER;


    @Override
    public String toString() {
        return switch (this) {
            case TECHNICIAN -> "Technician";
            case PROGRAMMER -> "Programmer";
            case OPERATIONS_MANAGER -> "Operations Manager";
            case PROJECT_MANAGER -> "Project Manager";
        };
    }
}
