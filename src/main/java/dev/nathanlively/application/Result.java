package dev.nathanlively.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public sealed abstract class Result<SUCCESS> {

    private static final Logger log = LoggerFactory.getLogger(Result.class);

    static <SUCCESS> Result<SUCCESS> success(SUCCESS value) {
        return new SuccessResult<>(List.of(value));
    }

    static <SUCCESS> Result<SUCCESS> success(List<SUCCESS> values) {
        return new SuccessResult<>(List.copyOf(values));
    }

    static <SUCCESS> Result<SUCCESS> failure(String message) {
        log.error(message);
        return new FailureResult<>(Collections.singletonList(message));
    }

    static <SUCCESS> Result<SUCCESS> failure(List<String> failureMessages) {
        failureMessages.forEach(log::error);
        return new FailureResult<>(failureMessages);
    }

    public abstract List<SUCCESS> values();

    public abstract boolean isSuccess();

    public boolean isFailure() {
        return !isSuccess();
    }

    public abstract List<String> failureMessages();

    private static final class SuccessResult<SUCCESS> extends Result<SUCCESS> {
        private final List<SUCCESS> values = new ArrayList<>();

        private SuccessResult(List<SUCCESS> values) {
            this.values.addAll(values);
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public List<SUCCESS> values() {
            return values;
        }

        @Override
        public List<String> failureMessages() {
            return Collections.emptyList();
        }
    }

    private static final class FailureResult<SUCCESS> extends Result<SUCCESS> {
        private final List<String> failureMessages = new ArrayList<>();

        private FailureResult(List<String> failureMessage) {
            this.failureMessages.addAll(failureMessage);
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public List<SUCCESS> values() {
            return Collections.emptyList();
        }

        @Override
        public List<String> failureMessages() {
            return List.copyOf(failureMessages);
        }
    }
}
