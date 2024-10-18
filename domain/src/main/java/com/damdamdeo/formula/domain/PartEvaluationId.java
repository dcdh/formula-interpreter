package com.damdamdeo.formula.domain;

import java.util.Objects;

public record PartEvaluationId(Integer id) implements Comparable<PartEvaluationId> {
    public PartEvaluationId {
        Objects.requireNonNull(id);
    }

    public PartEvaluationId increment() {
        return new PartEvaluationId(id + 1);
    }

    @Override
    public int compareTo(final PartEvaluationId partEvaluationId) {
        return id.compareTo(partEvaluationId.id());
    }
}