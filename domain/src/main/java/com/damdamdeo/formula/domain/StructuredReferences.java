package com.damdamdeo.formula.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record StructuredReferences(List<StructuredReference> structuredData) {

    public StructuredReferences() {
        this(Collections.emptyList());
    }

    public StructuredReferences(final List<StructuredReference> structuredData) {
        this.structuredData = Objects.requireNonNull(structuredData);
    }

    public Value getValueByReference(final Reference reference) throws UnknownReferenceException {
        return structuredData.stream()
                .filter(structuredDatum -> structuredDatum.reference().equals(reference))
                .findFirst()
                .map(StructuredReference::value)
                .orElseThrow(() -> new UnknownReferenceException(reference));
    }

}
