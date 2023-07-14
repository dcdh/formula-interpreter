package com.damdamdeo.formula.infrastructure.antlr.autosuggest;

import com.damdamdeo.formula.domain.SuggestedFormula;

import java.util.Objects;

public class AutoSuggestUnavailableException extends RuntimeException {
    private final SuggestedFormula suggestedFormula;

    public AutoSuggestUnavailableException(final SuggestedFormula suggestedFormula, final Throwable cause) {
        super(cause);
        this.suggestedFormula = Objects.requireNonNull(suggestedFormula);
    }

    public SuggestedFormula suggestedFormula() {
        return suggestedFormula;
    }
}
