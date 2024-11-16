package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;

public record SuggestionsCompletion(List<String> tokens) {
    public SuggestionsCompletion {
        Objects.requireNonNull(tokens);
    }
}
