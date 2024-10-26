package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.evaluation.Expression;

import java.util.Objects;

public record ProcessingResult(Expression expression,
                               ParserEvaluationProcessedIn parserEvaluationProcessedIn) {
    public ProcessingResult {
        Objects.requireNonNull(expression);
    }
}
