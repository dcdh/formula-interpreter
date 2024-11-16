package com.damdamdeo.formula.domain;

import java.time.ZonedDateTime;
import java.util.Objects;

public record ProcessedAt(ZonedDateTime at) implements ProcessedAtStart, ProcessedAtEnd {
    public ProcessedAt {
        Objects.requireNonNull(at);
    }
}
