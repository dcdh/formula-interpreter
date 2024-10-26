package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.LogicalBooleanFunction;
import com.damdamdeo.formula.domain.Range;

import java.util.Objects;

public record LogicalBooleanExpression(LogicalBooleanFunction.Function logicalBooleanFunction, Expression left,
                                       Expression right, Range range) implements Expression {
    public LogicalBooleanExpression {
        Objects.requireNonNull(logicalBooleanFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(range);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
