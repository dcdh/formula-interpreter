package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.PositionedAt;
import com.damdamdeo.formula.domain.Reference;

import java.util.Objects;

public record StructuredReferencesExpression(Reference reference, PositionedAt positionedAt) implements Expression {
    public StructuredReferencesExpression {
        Objects.requireNonNull(reference);
        Objects.requireNonNull(positionedAt);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }
}
