package com.damdamdeo.formula.domain;

public final class ExecutionException extends RuntimeException {
    public ExecutionException(final SyntaxErrorException cause) {
        super(cause);
    }

    public ExecutionException(final Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return getCause().getMessage();
    }

}
