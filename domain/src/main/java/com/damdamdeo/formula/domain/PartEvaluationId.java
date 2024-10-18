package com.damdamdeo.formula.domain;

import java.util.concurrent.atomic.AtomicInteger;

public record PartEvaluationId(Integer id) implements Comparable<PartEvaluationId> {
    public PartEvaluationId(final AtomicInteger currentPartEvaluationId) {
        this(currentPartEvaluationId.addAndGet(1));
    }

    // TODO add an increment synchronized method
    @Override
    public int compareTo(final PartEvaluationId partEvaluationId) {
        return id.compareTo(partEvaluationId.id());
    }
}