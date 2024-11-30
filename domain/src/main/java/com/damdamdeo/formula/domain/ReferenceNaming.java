package com.damdamdeo.formula.domain;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public record ReferenceNaming(String name) {
    public ReferenceNaming {
        Objects.requireNonNull(name);
        Validate.validState(!name.startsWith("[@["));
        Validate.validState(!name.endsWith("]]"));
    }
}
