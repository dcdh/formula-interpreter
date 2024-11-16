package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.SuggestedFormula;

import java.util.Objects;

public class AntlrAutoSuggestionExecutionTimedOutException extends Exception {
    private final SuggestedFormula suggestedFormula;

    public AntlrAutoSuggestionExecutionTimedOutException(final SuggestedFormula suggestedFormula, final Throwable cause) {
        super(cause);
        this.suggestedFormula = Objects.requireNonNull(suggestedFormula);
    }

    @Override
    public String getMessage() {
        return String.format("AutoSuggestion service has timed out while executing formula '%s' - Infinite loop in Grammar - msg '%s'",
                suggestedFormula.formula(),
                getCause().getMessage());
    }

    public SuggestedFormula suggestedFormula() {
        return suggestedFormula;
    }

}
