package com.damdamdeo.formula.domain;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public record RangeEnd(Integer end) {
    public RangeEnd {
        Objects.requireNonNull(end);
        Validate.validState(end >= 0, "End must be greater or equal to zero: %d", end);
    }

    public RangeEnd() {
        this(0);
    }

    public boolean isInitialValue() {
        return end == 0;
    }

    public RangeEnd add(final Integer add) {
        Objects.requireNonNull(add);
        return new RangeEnd(end + add);
    }
}
