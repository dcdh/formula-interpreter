package com.damdamdeo.formula.domain;

import org.apache.commons.lang3.Validate;

import java.util.Objects;
import java.util.regex.Pattern;

public record Reference(String reference) implements InputValue {
    private static final Pattern REFERENCE_PATTERN = Pattern.compile("^(.+)$");

    public Reference {
        Objects.requireNonNull(reference);
        Validate.isTrue(REFERENCE_PATTERN.matcher(reference).matches());
    }

    @Override
    public String value() {
        return reference;
    }
}
