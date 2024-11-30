package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.domain.*;

import java.util.List;
import java.util.Objects;

public record ArgumentExpression(Argument argument, PositionedAt positionedAt) implements Expression {
    public ArgumentExpression {
        Objects.requireNonNull(argument);
        Objects.requireNonNull(positionedAt);
    }

    @Override
    public Evaluated accept(final ExpressionVisitor visitor) {
        return visitor.visit(this);
    }

    public Value resolveArgument(final List<StructuredReference> structuredReferences) {
        return argument.resolveArgument(structuredReferences);
    }

    public Reference reference() {
        return argument.reference();
    }
}
