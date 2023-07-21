package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxErrorException;

public final class ExecutionException extends RuntimeException {
    public ExecutionException(final AntlrSyntaxErrorException cause) {
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
