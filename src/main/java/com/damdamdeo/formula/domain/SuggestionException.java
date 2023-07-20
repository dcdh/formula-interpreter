package com.damdamdeo.formula.domain;

public final class SuggestionException extends RuntimeException {
    public SuggestionException(final AutoSuggestUnavailableException exception) {
        super(exception);
    }

    public SuggestionException(final AutoSuggestionExecutionException exception) {
        super(exception);
    }

    public SuggestionException(final AutoSuggestionExecutionTimedOutException exception) {
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
