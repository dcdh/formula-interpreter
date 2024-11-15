package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.StructuredReference;
import com.damdamdeo.formula.domain.evaluation.Expression;

import java.util.List;
import java.util.Objects;

public record Given(Formula formula,
                    Expression expression,
                    List<StructuredReference> structuredReferences) {
    public Given {
        Objects.requireNonNull(formula);
        Objects.requireNonNull(expression);
        Objects.requireNonNull(structuredReferences);
    }
}
