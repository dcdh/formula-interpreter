package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.IntermediateResult;

import java.util.List;
import java.util.Objects;

public record IntermediateResults(List<IntermediateResult> intermediateResult) {
    public IntermediateResults {
        Objects.requireNonNull(intermediateResult);
    }
}
