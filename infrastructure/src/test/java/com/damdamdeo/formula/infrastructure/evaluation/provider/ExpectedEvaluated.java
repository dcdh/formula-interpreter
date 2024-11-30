package com.damdamdeo.formula.infrastructure.evaluation.provider;

import com.damdamdeo.formula.domain.Evaluated;

import java.util.Objects;

public record ExpectedEvaluated(Evaluated evaluated) {
    public ExpectedEvaluated {
        Objects.requireNonNull(evaluated);
    }
}
