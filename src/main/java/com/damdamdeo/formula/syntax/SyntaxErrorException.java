package com.damdamdeo.formula.syntax;

import java.util.Objects;

public final class SyntaxErrorException extends Exception {

    private final SyntaxError syntaxError;

    public SyntaxErrorException(final SyntaxError syntaxError) {
        this.syntaxError = Objects.requireNonNull(syntaxError);
    }

    public SyntaxError syntaxError() {
        return syntaxError;
    }

}
