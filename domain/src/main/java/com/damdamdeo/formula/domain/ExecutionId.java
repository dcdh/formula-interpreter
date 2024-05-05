package com.damdamdeo.formula.domain;

import java.util.concurrent.atomic.AtomicInteger;

public record ExecutionId(Integer id) implements Comparable<ExecutionId> {
    public ExecutionId(final AtomicInteger currentExecutionId) {
        this(currentExecutionId.addAndGet(1));
    }

    @Override
    public int compareTo(final ExecutionId executionId) {
        return id.compareTo(executionId.id());
    }
}