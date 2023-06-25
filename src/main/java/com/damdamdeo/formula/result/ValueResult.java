package com.damdamdeo.formula.result;

import java.util.Objects;

public record ValueResult(String result) implements Result {
    public ValueResult(final String result) {
        this.result = Objects.requireNonNull(result);
    }

}
