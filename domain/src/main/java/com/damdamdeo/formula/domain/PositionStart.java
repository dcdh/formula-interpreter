package com.damdamdeo.formula.domain;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public record PositionStart(Integer start) {
    public PositionStart {
        Objects.requireNonNull(start);
        Validate.validState(start >= 0, "Start must be greater or equal to zero: %d", start);
    }

    public PositionStart() {
        this(0);
    }

    public boolean isValid(final PositionEnd positionEnd) {
        Objects.requireNonNull(positionEnd);
        return start == 0 && positionEnd.isInitialValue()
                || start <= positionEnd.end();
    }

    public PositionStart add(final Integer add) {
        Objects.requireNonNull(add);
        return new PositionStart(start + add);
    }
}
