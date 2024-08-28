package dev.nathanlively.application;

import dev.nathanlively.domain.TimesheetEntry;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.CollectionAssert;
import org.assertj.core.internal.Booleans;

public class ResultAssert extends AbstractAssert<ResultAssert, Result<TimesheetEntry>> {

    public ResultAssert(Result result) {
        super(result, ResultAssert.class);
    }

    public ResultAssert isSuccess() {
        isNotNull();

        Booleans.instance().assertEqual(info, actual.isSuccess(), true);

        return myself;
    }

    /**
     * WARNING: currently overrides any as() calls
     */
    public ResultAssert isFailure() {
        isNotNull();

        describedAs("Succeeded, but should have failed");
        Booleans.instance().assertEqual(info, actual.isSuccess(), false);

        return myself;
    }

    public CollectionAssert<TimesheetEntry> successValues() {
        return new CollectionAssert<>(actual.values());
    }

    public CollectionAssert<String> failureMessages() {
        return new CollectionAssert<>(actual.failureMessages());
    }
}
