package dev.nathanlively.security;

import org.springframework.data.annotation.Version;

public abstract class AbstractEntity {

    @Version
    private int version;

    public int getVersion() {
        return version;
    }

}