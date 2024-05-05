package com.damdamdeo.formula.domain;

import java.util.concurrent.Callable;

public final class NoOpExecutionWrapper implements ExecutionWrapper {
    @Override
    public Result execute(final Callable<Result> callable) {
        try {
            return callable.call();
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
