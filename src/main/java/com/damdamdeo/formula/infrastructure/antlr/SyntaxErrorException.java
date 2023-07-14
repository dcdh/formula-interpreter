package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Formula;

import java.util.Objects;

public final class SyntaxErrorException extends RuntimeException {

    private final Formula formula;
    private final AntlrSyntaxError antlrSyntaxError;

    public SyntaxErrorException(final Formula formula,
                                final AntlrSyntaxError antlrSyntaxError) {
        this.formula = Objects.requireNonNull(formula);
        this.antlrSyntaxError = Objects.requireNonNull(antlrSyntaxError);
    }

    public AntlrSyntaxError syntaxError() {
        return antlrSyntaxError;
    }

    @Override
    public String toString() {
        return "SyntaxErrorException{" +
               "formula=" + formula +
               ", syntaxError=" + antlrSyntaxError +
               '}';
    }
}
