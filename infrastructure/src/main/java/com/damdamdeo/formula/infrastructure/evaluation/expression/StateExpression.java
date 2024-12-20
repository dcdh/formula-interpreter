package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.PositionedAt;
import com.damdamdeo.formula.domain.StateFunction;

import java.util.Objects;

public record StateExpression(StateFunction.Function stateFunction, Expression expression,
                              PositionedAt positionedAt) implements Expression {
    public StateExpression {
        Objects.requireNonNull(stateFunction);
        Objects.requireNonNull(expression);
        Objects.requireNonNull(positionedAt);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
