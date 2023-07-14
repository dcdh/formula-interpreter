package com.damdamdeo.formula.infrastructure.antlr.autosuggest;

import com.damdamdeo.formula.domain.SuggestedFormula;

import java.util.Objects;

public class AutoSuggestionExecutionException extends RuntimeException {
    private final SuggestedFormula suggestedFormula;

    public AutoSuggestionExecutionException(final SuggestedFormula suggestedFormula, final Throwable cause) {
        super(cause);
        this.suggestedFormula = Objects.requireNonNull(suggestedFormula);
    }

    public SuggestedFormula suggestedFormula() {
        return suggestedFormula;
    }
}
