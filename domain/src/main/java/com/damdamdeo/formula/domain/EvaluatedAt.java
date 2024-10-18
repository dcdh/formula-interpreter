package com.damdamdeo.formula.domain;

import java.time.ZonedDateTime;
import java.util.Objects;

public record EvaluatedAt(ZonedDateTime at) implements EvaluatedAtStart, EvaluatedAtEnd {
    public EvaluatedAt {
        Objects.requireNonNull(at);
    }
}
