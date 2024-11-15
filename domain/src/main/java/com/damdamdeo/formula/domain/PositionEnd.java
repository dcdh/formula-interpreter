package com.damdamdeo.formula.domain;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public record PositionEnd(Integer end) {
    public PositionEnd {
        Objects.requireNonNull(end);
// FCK        Validate.validState(end >= 0, "End must be greater or equal to zero: %d", end);
    }

    public PositionEnd() {
        this(0);
    }

    public boolean isInitialValue() {
        return end == 0;
    }

    public PositionEnd add(final Integer add) {
        Objects.requireNonNull(add);
        return new PositionEnd(end + add);
    }
}
