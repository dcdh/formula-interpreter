package com.damdamdeo.formula.infrastructure.api;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Objects;

@Schema(name = "Suggestions", required = true, requiredProperties = {"tokens"})
public record SuggestionsDTO(@Schema(required = true) List<String> tokens) {
    public SuggestionsDTO {
        Objects.requireNonNull(tokens);
    }
}
