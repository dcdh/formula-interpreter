package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.Range;
import com.damdamdeo.formula.domain.Value;

import java.util.Objects;

public record ArgumentExpression(Value value, Range range) implements Expression {
    public ArgumentExpression {
        Objects.requireNonNull(value);
        Objects.requireNonNull(range);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
