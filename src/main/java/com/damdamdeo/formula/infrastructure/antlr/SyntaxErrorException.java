package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Formula;

import java.util.Objects;

public final class SyntaxErrorException extends RuntimeException {

    private final Formula formula;
    private final SyntaxError syntaxError;

    public SyntaxErrorException(final Formula formula,
                                final SyntaxError syntaxError) {
        this.formula = Objects.requireNonNull(formula);
        this.syntaxError = Objects.requireNonNull(syntaxError);
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
