package dev.nathanlively.domain;

import java.util.Objects;

public abstract class Named implements Comparable<Named> {
    private final String name;

    protected Named(final String name) {
        super();
        this.name = Objects.requireNonNull(name, () -> "Name cannot be empty");
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
