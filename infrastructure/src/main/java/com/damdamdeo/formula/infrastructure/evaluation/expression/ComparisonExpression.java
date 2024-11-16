package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.domain.ComparisonFunction;
import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.PositionedAt;

import java.util.Objects;

public record ComparisonExpression(ComparisonFunction.Comparison comparisonFunction, Expression left,
                                   Expression right, PositionedAt positionedAt) implements Expression {
    public ComparisonExpression {
        Objects.requireNonNull(comparisonFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(positionedAt);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
