package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.domain.*;

import java.util.List;
import java.util.Objects;

public record StructuredReferenceExpression(Reference reference, PositionedAt positionedAt) implements Expression {

    public StructuredReferenceExpression {
        Objects.requireNonNull(reference);
        Objects.requireNonNull(positionedAt);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }

    public Value resolveValue(final List<StructuredReference> structuredReferences) {
        Objects.requireNonNull(structuredReferences);
        Objects.requireNonNull(reference);
        return Value.ofStructuredReference(reference, structuredReferences);
    }

}
