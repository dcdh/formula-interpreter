package com.damdamdeo.formula.domain;

import java.util.Objects;

public class AutoSuggestionExecutionException extends RuntimeException {
    private final SuggestedFormula suggestedFormula;

    public AutoSuggestionExecutionException(final SuggestedFormula suggestedFormula, final Throwable cause) {
        super(cause);
        this.suggestedFormula = Objects.requireNonNull(suggestedFormula);
    }

    @Override
    public String getMessage() {
        return String.format("AutoSuggestion service execution exception while processing formula '%s' - msg '%s'",
                suggestedFormula.formula(),
                getCause().getMessage());
    }

    public SuggestedFormula suggestedFormula() {
        return suggestedFormula;
    }
}