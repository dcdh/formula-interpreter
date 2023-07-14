package com.damdamdeo.formula.infrastructure.antlr;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public final class SyntaxErrorListener extends BaseErrorListener {

    private AntlrSyntaxError antlrSyntaxError;

    @Override
    public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, final int line, final int charPositionInLine, final String msg, final RecognitionException e) {
        this.antlrSyntaxError = new AntlrSyntaxError(line, charPositionInLine, msg);
    }

    public boolean hasSyntaxError() {
        return this.antlrSyntaxError != null;
    }

    public AntlrSyntaxError syntaxError() {
        return antlrSyntaxError;
    }
}
