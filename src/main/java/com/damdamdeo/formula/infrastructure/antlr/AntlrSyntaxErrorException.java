package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Formula;

import java.util.Objects;

public final class AntlrSyntaxErrorException extends RuntimeException {

    private final Formula formula;
    private final AntlrSyntaxError syntaxError;

    public AntlrSyntaxErrorException(final Formula formula,
                                     final AntlrSyntaxError syntaxError) {
        this.formula = Objects.requireNonNull(formula);
        this.syntaxError = Objects.requireNonNull(syntaxError);
    }

    @Override
    public String getMessage() {
        return syntaxError.toString();
    }

    public AntlrSyntaxError syntaxError() {
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
