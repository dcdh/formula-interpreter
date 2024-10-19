package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;

public record StructuredReferencesFunction(List<StructuredReference> structuredReferences,
                                           Reference reference) implements Function {

    public StructuredReferencesFunction {
        Objects.requireNonNull(structuredReferences);
        Objects.requireNonNull(reference);
    }

    @Override
    public Value evaluate(final NumericalContext numericalContext) {
        Objects.requireNonNull(numericalContext);
        return structuredReferences.stream()
                .filter(structuredDatum -> structuredDatum.reference().equals(reference))
                .findFirst()
                .map(StructuredReference::value)
                .orElse(Value.ofUnknownRef());
    }
}
