package com.damdamdeo.formula.domain;

public final class SuggestionException extends RuntimeException {

    public SuggestionException(final Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return getCause().getMessage();
    }
}
