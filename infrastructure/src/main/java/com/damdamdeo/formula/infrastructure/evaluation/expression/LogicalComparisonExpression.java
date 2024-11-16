package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.LogicalComparisonFunction;
import com.damdamdeo.formula.domain.PositionedAt;

import java.util.Objects;

public record LogicalComparisonExpression(LogicalComparisonFunction.Function logicalComparisonFunction,
                                          Expression comparison, Expression onTrue, Expression onFalse,
                                          PositionedAt positionedAt) implements Expression {
    public LogicalComparisonExpression {
        Objects.requireNonNull(logicalComparisonFunction);
        Objects.requireNonNull(comparison);
        Objects.requireNonNull(onTrue);
        Objects.requireNonNull(onFalse);
        Objects.requireNonNull(positionedAt);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
