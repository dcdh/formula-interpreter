package com.damdamdeo.formula.domain;

public final class EvaluationException extends RuntimeException {

    public EvaluationException(final Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return getCause().getMessage();
    }

}
