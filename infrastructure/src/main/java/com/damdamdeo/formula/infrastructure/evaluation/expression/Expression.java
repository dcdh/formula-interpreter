package com.damdamdeo.formula.infrastructure.evaluation.expression;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.PositionedAt;

public interface Expression {
    PositionedAt positionedAt();

    Evaluated accept(ExpressionVisitor visitor);
}
