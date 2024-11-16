package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.domain.ArithmeticFunction;
import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.PositionedAt;

import java.util.Objects;

public record ArithmeticExpression(ArithmeticFunction.Function arithmeticFunction, Expression left,
                                   Expression right, PositionedAt positionedAt) implements Expression {
    public ArithmeticExpression {
        Objects.requireNonNull(arithmeticFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(positionedAt);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
