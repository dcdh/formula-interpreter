package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.ComparisonFunction;
import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.Range;

import java.util.Objects;

public record ComparisonExpression(ComparisonFunction.Comparison comparisonFunction, Expression left,
                                   Expression right, Range range) implements Expression {
    public ComparisonExpression {
        Objects.requireNonNull(comparisonFunction);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(range);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
