package com.damdamdeo.formula.infrastructure.parser.antlr;

import com.damdamdeo.formula.domain.Formula;

import java.util.Objects;

public final class AntlrSyntaxErrorException extends RuntimeException {

    private final Formula formula;
    private final AntlrSyntaxError antlrSyntaxError;

    public AntlrSyntaxErrorException(final Formula formula,
                                     final AntlrSyntaxError antlrSyntaxError) {
        this.formula = Objects.requireNonNull(formula);
        this.antlrSyntaxError = Objects.requireNonNull(antlrSyntaxError);
    }

    @Override
    public String getMessage() {
        return antlrSyntaxError.toString();
    }

    public AntlrSyntaxError antlrSyntaxError() {
        return antlrSyntaxError;
    }

    @Override
    public String toString() {
        return "SyntaxErrorException{" +
               "formula=" + formula +
               ", antlrSyntaxError=" + antlrSyntaxError +
               '}';
    }
}
