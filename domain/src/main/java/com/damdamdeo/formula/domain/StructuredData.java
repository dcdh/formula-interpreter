package com.damdamdeo.formula.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record StructuredData(List<StructuredDatum> structuredData) {

    public StructuredData() {
        this(Collections.emptyList());
    }

    public StructuredData(final List<StructuredDatum> structuredData) {
        this.structuredData = Objects.requireNonNull(structuredData);
    }

    public Value getValueByReference(final Reference reference) throws UnknownReferenceException {
        return structuredData.stream()
                .filter(structuredDatum -> structuredDatum.reference().equals(reference))
                .findFirst()
                .map(StructuredDatum::value)
                .orElseThrow(() -> new UnknownReferenceException(reference));
    }

}
