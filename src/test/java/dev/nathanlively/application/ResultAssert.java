package dev.nathanlively.application;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.CollectionAssert;
import org.assertj.core.internal.Booleans;

public class ResultAssert<T> extends AbstractAssert<ResultAssert<T>, Result<T>> {

    public ResultAssert(Result<T> result) {
        super(result, ResultAssert.class);
    }

    public ResultAssert<T> isSuccess() {
        isNotNull();
        Booleans.instance().assertEqual(info, actual.isSuccess(), true);
        return this;
    }

    public ResultAssert<T> isFailure() {
        isNotNull();
        describedAs("Succeeded, but should have failed");
        Booleans.instance().assertEqual(info, actual.isSuccess(), false);
        return this;
    }

    public CollectionAssert<T> successValues() {
        return new CollectionAssert<>(actual.values());
    }

    public CollectionAssert<String> failureMessages() {
        return new CollectionAssert<>(actual.failureMessages());
    }
}
