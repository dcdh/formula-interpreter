package com.damdamdeo.formula.domain;

import java.util.Objects;

public record Range(Integer start, Integer end) {
    public Range {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
    }
}
