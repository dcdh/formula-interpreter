package com.damdamdeo.formula.domain.evaluation;

import com.damdamdeo.formula.domain.Evaluated;
import com.damdamdeo.formula.domain.Range;

public interface Expression {
    Range range();

    Evaluated accept(ExpressionVisitor visitor);
}
