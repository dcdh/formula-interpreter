package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.SuggestedFormula;

import java.util.Objects;

public class AntlrAutoSuggestionExecutionException extends Exception {
    private final SuggestedFormula suggestedFormula;

    public AntlrAutoSuggestionExecutionException(final SuggestedFormula suggestedFormula, final Throwable cause) {
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
