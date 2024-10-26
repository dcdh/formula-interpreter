package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.PositionedAt;
import com.damdamdeo.formula.domain.Value;

import java.util.Objects;

public record ArgumentExpression(Value value, PositionedAt positionedAt) implements Expression {
    public ArgumentExpression {
        Objects.requireNonNull(value);
        Objects.requireNonNull(positionedAt);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
