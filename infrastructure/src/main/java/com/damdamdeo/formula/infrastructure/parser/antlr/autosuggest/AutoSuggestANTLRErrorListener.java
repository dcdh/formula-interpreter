package com.damdamdeo.formula.infrastructure.parser.antlr.autosuggest;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.Objects;

public final class AutoSuggestANTLRErrorListener extends BaseErrorListener {
    private final String input;
    private String unTokenizedText = "";

    public AutoSuggestANTLRErrorListener(final String input) {
        this.input = Objects.requireNonNull(input);
    }

    @Override
    public void syntaxError(final Recognizer<?, ?> recognizer,
                            final Object offendingSymbol,
                            final int line,
                            final int charPositionInLine,
                            final String msg,
                            final RecognitionException e) throws ParseCancellationException {
        unTokenizedText = input.substring(charPositionInLine); // intended side effect
    }

    public String unTokenizedText() {
        return unTokenizedText;
    }
}
