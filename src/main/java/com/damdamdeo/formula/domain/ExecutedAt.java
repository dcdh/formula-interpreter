package com.damdamdeo.formula.domain;

import java.time.ZonedDateTime;
import java.util.Objects;

public record ExecutedAt(ZonedDateTime at) implements ExecutedAtStart, ExecutedAtEnd {
    public ExecutedAt {
        Objects.requireNonNull(at);
    }
}
