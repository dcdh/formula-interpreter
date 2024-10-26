package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.LogicalComparisonFunction;
import com.damdamdeo.formula.domain.Range;

import java.util.Objects;

public record LogicalComparisonExpression(LogicalComparisonFunction.Function logicalComparisonFunction,
                                          Expression comparison, Expression onTrue, Expression onFalse,
                                          Range range) implements Expression {
    public LogicalComparisonExpression {
        Objects.requireNonNull(logicalComparisonFunction);
        Objects.requireNonNull(comparison);
        Objects.requireNonNull(onTrue);
        Objects.requireNonNull(onFalse);
        Objects.requireNonNull(range);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
