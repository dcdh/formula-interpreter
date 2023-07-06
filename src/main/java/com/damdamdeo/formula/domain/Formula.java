package com.damdamdeo.formula.domain;

import java.util.Objects;

public record Formula(String formula) {
    public Formula {
        Objects.requireNonNull(formula);
    }
}
