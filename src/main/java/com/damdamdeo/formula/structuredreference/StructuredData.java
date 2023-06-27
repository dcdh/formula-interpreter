package com.damdamdeo.formula.structuredreference;

import com.damdamdeo.formula.Value;

import java.util.Collections;
import java.util.List;

public record StructuredData(List<StructuredDatum> structuredData) {

    public StructuredData() {
        this(Collections.emptyList());
    }

    public Value getValueByReference(final Reference reference) throws UnknownReferenceException {
        return structuredData.stream()
                .filter(structuredDatum -> structuredDatum.reference().equals(reference))
                .findFirst()
                .map(StructuredDatum::value)
                .orElseThrow(() -> new UnknownReferenceException(reference));
    }

}

