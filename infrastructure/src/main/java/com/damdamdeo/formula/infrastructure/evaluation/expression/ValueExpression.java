package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.PositionedAt;
import com.damdamdeo.formula.domain.Value;

import java.util.Objects;

public record ValueExpression(Value value, PositionedAt positionedAt) implements Expression {
    public ValueExpression {
        Objects.requireNonNull(value);
        Objects.requireNonNull(positionedAt);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
