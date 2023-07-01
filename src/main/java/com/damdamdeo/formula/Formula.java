package com.damdamdeo.formula;

import java.util.Objects;

public record Formula(String formula) {

    public Formula(final String formula) {
        this.formula = Objects.requireNonNull(formula);
    }

    @Override
    public String toString() {
        return "Formula{" +
                "formula='" + formula + '\'' +
                '}';
    }
}
