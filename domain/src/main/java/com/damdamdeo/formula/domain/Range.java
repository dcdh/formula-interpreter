package com.damdamdeo.formula.domain;

import java.util.Objects;

public record Range(Integer start, Integer end) {
    public Range {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
    }

    public Range of(final Integer plusStart, final Integer plusEnd) {
        Objects.requireNonNull(plusStart);
        Objects.requireNonNull(plusEnd);
        return new Range(start + plusStart, end + plusEnd);
    }
}
