package com.damdamdeo.formula.infrastructure.antlr.autosuggest;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionLexerAndParserFactory implements LexerAndParserFactory {

    private final Constructor<? extends Lexer> lexerCtr;
    private final Constructor<? extends Parser> parserCtr;

    public ReflectionLexerAndParserFactory(final Class<? extends Lexer> lexerClass,
                                           final Class<? extends Parser> parserClass) {
        lexerCtr = getConstructor(lexerClass, CharStream.class);
        parserCtr = getConstructor(parserClass, TokenStream.class);
    }

    @Override
    public Lexer createLexer(final CharStream input) {
        return create(lexerCtr, input);
    }

    @Override
    public Parser createParser(final TokenStream tokenStream) {
        return create(parserCtr, tokenStream);
    }

    private static <T> Constructor<? extends T> getConstructor(final Class<? extends T> givenClass, final Class<?> argClass) {
        try {
            return givenClass.getConstructor(argClass);
        } catch (final NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(
                    givenClass.getSimpleName() + " must have constructor from " + argClass.getSimpleName() + ".");
        }
    }

    private <T> T create(final Constructor<? extends T> constructor, final Object arg) {
        try {
            return constructor.newInstance(arg);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
