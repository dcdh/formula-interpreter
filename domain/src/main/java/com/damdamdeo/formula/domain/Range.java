package com.damdamdeo.formula.domain;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public record Range(RangeStart rangeStart, RangeEnd rangeEnd) {
    public Range {
        Objects.requireNonNull(rangeStart);
        Objects.requireNonNull(rangeEnd);
        Validate.validState(rangeStart.isValid(rangeEnd), "Range is invalid start: %d, end: %d", rangeStart.start(), rangeEnd.end());
    }

    public Range() {
        this(new RangeStart(), new RangeEnd());
    }

    @Deprecated(forRemoval = true)
    public Range(final Integer start, final Integer end) {
        this(new RangeStart(start), new RangeEnd(end));
    }

    public Range of(final Integer addStart, final Integer addEnd) {
        Objects.requireNonNull(addStart);
        Objects.requireNonNull(addEnd);
        return new Range(rangeStart.add(addStart), rangeEnd.add(addEnd));
    }
}
