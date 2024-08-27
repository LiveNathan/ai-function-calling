package dev.nathanlively.domain;

public enum ResourceType {
    FULL_TIME,
    PART_TIME,
    CONTRACTOR,
    INTERN;

    @Override
    public String toString() {
        return switch (this) {
            case FULL_TIME -> "Full-Time";
            case PART_TIME -> "Part-Time";
            case CONTRACTOR -> "Contractor";
            case INTERN -> "Intern";
        };
    }
}
