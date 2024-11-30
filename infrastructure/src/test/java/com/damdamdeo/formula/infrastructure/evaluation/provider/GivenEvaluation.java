package com.damdamdeo.formula.infrastructure.evaluation.provider;

import com.damdamdeo.formula.domain.Formula;

import java.util.Objects;

public record GivenEvaluation(Formula formula) {
    public GivenEvaluation {
        Objects.requireNonNull(formula);
    }
}