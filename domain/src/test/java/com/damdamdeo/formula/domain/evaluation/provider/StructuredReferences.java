package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.StructuredReference;

import java.util.List;
import java.util.Objects;

public record StructuredReferences(List<StructuredReference> structuredReferences) {
    public StructuredReferences {
        Objects.requireNonNull(structuredReferences);
    }
}
