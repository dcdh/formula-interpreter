package com.damdamdeo.formula.domain;

import java.util.concurrent.Callable;

public final class NoOpExecutionWrapper implements ExecutionWrapper {
    @Override
    public Value execute(final Callable<ContextualResult> callable) {
        try {
            return callable.call().result();
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
