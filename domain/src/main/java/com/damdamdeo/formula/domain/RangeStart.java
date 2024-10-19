package com.damdamdeo.formula.domain;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public record RangeStart(Integer start) {
    public RangeStart {
        Objects.requireNonNull(start);
        Validate.validState(start >= 0, "Start must be greater or equal to zero: %d", start);
    }

    public RangeStart() {
        this(0);
    }

    public boolean isValid(final RangeEnd rangeEnd) {
        Objects.requireNonNull(rangeEnd);
        return start == 0 && rangeEnd.isInitialValue()
                || start <= rangeEnd.end();
    }

    public RangeStart add(final Integer add) {
        Objects.requireNonNull(add);
        return new RangeStart(start + add);
    }
}
