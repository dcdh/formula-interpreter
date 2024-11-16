package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.LogicalBooleanFunction;
import com.damdamdeo.formula.domain.PositionedAt;

import java.util.Objects;

public record LogicalBooleanExpression(LogicalBooleanFunction.Function logicalBooleanFunction, Expression left,
                                       Expression right, PositionedAt positionedAt) implements Expression {
    public LogicalBooleanExpression {
        Objects.requireNonNull(logicalBooleanFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(positionedAt);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
