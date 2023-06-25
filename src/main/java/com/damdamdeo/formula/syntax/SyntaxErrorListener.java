package com.damdamdeo.formula.syntax;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public final class SyntaxErrorListener extends BaseErrorListener {

    private SyntaxError syntaxError;
    @Override
    public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, final int line, final int charPositionInLine, final String msg, final RecognitionException e) {
        this.syntaxError = new SyntaxError(line, charPositionInLine, msg);
    }

    public boolean hasSyntaxError() {
        return this.syntaxError != null;
    }
    public SyntaxError syntaxError() {
        return syntaxError;
    }
}
