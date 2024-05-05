package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.SuggestedFormula;

import java.util.Objects;

public class AntlrAutoSuggestUnavailableException extends Exception {
    private final SuggestedFormula suggestedFormula;

    public AntlrAutoSuggestUnavailableException(final SuggestedFormula suggestedFormula, final Throwable cause) {
        super(cause);
        this.suggestedFormula = Objects.requireNonNull(suggestedFormula);
    }

    @Override
    public String getMessage() {
        return String.format("AutoSuggestion service execution unavailable while processing formula '%s' - msg '%s'",
                suggestedFormula.formula(),
                getCause().getMessage());
    }

    public SuggestedFormula suggestedFormula() {
        return suggestedFormula;
    }
}
