package com.damdamdeo.formula.domain;

public final class ValidationException extends RuntimeException {
    public ValidationException(final Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return getCause().getMessage();
    }
}
