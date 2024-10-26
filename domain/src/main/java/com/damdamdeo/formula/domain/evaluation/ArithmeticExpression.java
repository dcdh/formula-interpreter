package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.ArithmeticFunction;
import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.Range;

import java.util.Objects;

public record ArithmeticExpression(ArithmeticFunction.Function arithmeticFunction, Expression left,
                                   Expression right, Range range) implements Expression {
    public ArithmeticExpression {
        Objects.requireNonNull(arithmeticFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(range);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
