package com.damdamdeo.formula.domain;

import java.time.ZonedDateTime;
import java.util.Objects;

public record ExecutedAt(ZonedDateTime at) {
    public ExecutedAt {
        Objects.requireNonNull(at);
    }
}
