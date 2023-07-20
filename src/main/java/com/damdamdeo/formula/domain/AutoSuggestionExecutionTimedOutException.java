package com.damdamdeo.formula.domain;

import java.util.Objects;

public class AutoSuggestionExecutionTimedOutException extends RuntimeException {
    private final SuggestedFormula suggestedFormula;

    public AutoSuggestionExecutionTimedOutException(final SuggestedFormula suggestedFormula, final Throwable cause) {
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
