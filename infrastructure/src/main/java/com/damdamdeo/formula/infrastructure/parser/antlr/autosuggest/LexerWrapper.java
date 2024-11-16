package com.damdamdeo.formula.infrastructure.parser.antlr.autosuggest;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNState;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

public class LexerWrapper {
    private final LexerFactory lexerFactory;
    private final Lexer cachedLexer;

    public LexerWrapper(final LexerFactory lexerFactory) {
        super();
        this.lexerFactory = lexerFactory;
        this.cachedLexer = createLexer("");
    }

    public TokenizationResult tokenizeNonDefaultChannel(final String input) {
        final TokenizationResult result = this.tokenize(input);
        return new TokenizationResult(
                result.tokens().stream().filter(t -> t.getChannel() == 0).collect(Collectors.toList()),
                result.untokenizedText()
        );
    }

    public String[] getRuleNames() {
        return cachedLexer.getRuleNames();
    }

    public ATNState findStateByRuleNumber(final int ruleNumber) {
        return cachedLexer.getATN().ruleToStartState[ruleNumber];
    }

    public Vocabulary getVocabulary() {
        return cachedLexer.getVocabulary();
    }

    private TokenizationResult tokenize(final String input) {
        final Lexer lexer = this.createLexer(input);
        lexer.removeErrorListeners();
        final AutoSuggestANTLRErrorListener autoSuggestANTLRErrorListener = new AutoSuggestANTLRErrorListener(input);
        lexer.addErrorListener(autoSuggestANTLRErrorListener);
        final List<? extends Token> tokens = lexer.getAllTokens();
        return new TokenizationResult(
                tokens,
                autoSuggestANTLRErrorListener.unTokenizedText()
        );
    }

    private Lexer createLexer(final CharStream input) {
        return this.lexerFactory.createLexer(input);
    }

    private Lexer createLexer(final String lexerInput) {
        return this.createLexer(toCharStream(lexerInput));
    }

    private static CharStream toCharStream(final String text) {
        final CharStream inputStream;
        try {
            inputStream = CharStreams.fromReader(new StringReader(text));
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected while reading input string", e);
        }
        return inputStream;
    }

}
