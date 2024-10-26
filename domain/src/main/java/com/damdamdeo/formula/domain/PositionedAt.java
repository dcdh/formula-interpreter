package com.damdamdeo.formula.domain;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public record PositionedAt(PositionStart positionStart, PositionEnd positionEnd) {
    public PositionedAt {
        Objects.requireNonNull(positionStart);
        Objects.requireNonNull(positionEnd);
        Validate.validState(positionStart.isValid(positionEnd), "Positioned at is invalid start: %d, end: %d", positionStart.start(), positionEnd.end());
    }

    public PositionedAt() {
        this(new PositionStart(), new PositionEnd());
    }

    public PositionedAt(final Integer start, final Integer end) {
        this(new PositionStart(start), new PositionEnd(end));
    }

    public PositionedAt of(final Integer addStart, final Integer addEnd) {
        Objects.requireNonNull(addStart);
        Objects.requireNonNull(addEnd);
        return new PositionedAt(positionStart.add(addStart), positionEnd.add(addEnd));
    }
}
