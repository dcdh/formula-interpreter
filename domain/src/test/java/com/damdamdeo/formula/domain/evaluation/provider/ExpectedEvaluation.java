package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.IntermediateResult;

import java.util.List;
import java.util.Objects;

public record ExpectedEvaluation(Evaluated evaluated,
                                 List<IntermediateResult> intermediateResults) {
    public ExpectedEvaluation {
        Objects.requireNonNull(evaluated);
        Objects.requireNonNull(intermediateResults);
    }

    public ExpectedEvaluation(final Evaluated evaluated) {
        this(evaluated, List.of());
    }
}