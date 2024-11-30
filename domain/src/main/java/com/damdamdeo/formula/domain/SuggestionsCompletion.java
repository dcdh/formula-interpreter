package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;

// TODO the token should be contextualized : UNKNOWN, VALUE, NUMERIC, BOOLEAN_TRUE, BOOLEAN_FALSE, STRUCTURED_REFERENCE, FUNCTION, COMMA, RIGHT_BRACKET, UNKNOWN
public record SuggestionsCompletion(List<String> tokens) {
    public SuggestionsCompletion {
        Objects.requireNonNull(tokens);
    }
}
