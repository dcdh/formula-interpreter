package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.evaluation.Expression;

import java.util.Objects;

public record MappingResult(Expression expression,
                            ParserEvaluationProcessedIn parserEvaluationProcessedIn) {
    public MappingResult {
        Objects.requireNonNull(expression);
    }
}
