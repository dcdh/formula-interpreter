package com.damdamdeo.formula.domain;

import java.util.Objects;

public record Position(Integer start, Integer end) {
    public Position {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
    }
}
