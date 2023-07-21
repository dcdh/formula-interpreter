package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.infrastructure.antlr.AntlrAutoSuggestUnavailableException;
import com.damdamdeo.formula.infrastructure.antlr.AntlrAutoSuggestionExecutionException;
import com.damdamdeo.formula.infrastructure.antlr.AntlrAutoSuggestionExecutionTimedOutException;

public final class SuggestionException extends RuntimeException {
    public SuggestionException(final AntlrAutoSuggestUnavailableException exception) {
        super(exception);
    }

    public SuggestionException(final AntlrAutoSuggestionExecutionException exception) {
        super(exception);
    }

    public SuggestionException(final AntlrAutoSuggestionExecutionTimedOutException exception) {
        super(exception);
    }

    public SuggestionException(final Exception exception) {
        super(exception);
    }

    @Override
    public String getMessage() {
        return getCause().getMessage();
    }
}
