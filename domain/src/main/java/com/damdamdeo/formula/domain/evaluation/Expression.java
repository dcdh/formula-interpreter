package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.PositionedAt;

public interface Expression {
    PositionedAt positionedAt();

    Evaluated accept(ExpressionVisitor visitor);
}
