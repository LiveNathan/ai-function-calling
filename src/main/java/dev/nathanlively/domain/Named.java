package dev.nathanlively.domain;

import static dev.nathanlively.common.validation.ValidationUtils.requireNonBlank;

public abstract class Named implements Comparable<Named> {
    private final String name;

    protected Named(final String name) {
        super();
        this.name = requireNonBlank(name, () -> "Name cannot be empty");
    }

    public String name() {
        return this.name;
    }

    @Override
    public int compareTo(final Named other) {
        return this.name().compareTo(other.name());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [" + this.name + "]";
    }
}
