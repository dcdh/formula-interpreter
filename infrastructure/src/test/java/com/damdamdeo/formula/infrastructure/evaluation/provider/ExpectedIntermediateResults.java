package com.damdamdeo.formula.infrastructure.evaluation.provider;

import com.damdamdeo.formula.domain.IntermediateResult;

import java.util.List;
import java.util.Objects;

public record ExpectedIntermediateResults(List<IntermediateResult> intermediateResults) {
    public ExpectedIntermediateResults {
        Objects.requireNonNull(intermediateResults);
    }
}