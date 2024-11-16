package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.domain.evaluation.AntlrMappingExpressionLoaded;

import java.util.Objects;

public record DefaultAntlrMappingExpressionLoaded(Expression expression) implements AntlrMappingExpressionLoaded {
    public DefaultAntlrMappingExpressionLoaded {
        Objects.requireNonNull(expression);
    }
}
