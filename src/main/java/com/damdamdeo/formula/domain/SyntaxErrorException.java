package com.damdamdeo.formula.domain;

import java.util.Objects;

public abstract class SyntaxErrorException extends RuntimeException {

    private final Formula formula;
    protected final SyntaxError syntaxError;

    public SyntaxErrorException(final Formula formula,
                                final SyntaxError syntaxError) {
        this.formula = Objects.requireNonNull(formula);
        this.syntaxError = Objects.requireNonNull(syntaxError);
    }

    @Override
    public String getMessage() {
        return syntaxError.toString();
    }

    public SyntaxError syntaxError() {
        return syntaxError;
    }

    @Override
    public String toString() {
        return "SyntaxErrorException{" +
               "formula=" + formula +
               ", syntaxError=" + syntaxError +
               '}';
    }
}
