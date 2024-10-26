package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.Range;
import com.damdamdeo.formula.domain.StateFunction;

import java.util.Objects;

public record StateExpression(StateFunction.Function stateFunction, Expression argument,
                              Range range) implements Expression {
    public StateExpression {
        Objects.requireNonNull(stateFunction);
        Objects.requireNonNull(argument);
        Objects.requireNonNull(range);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
