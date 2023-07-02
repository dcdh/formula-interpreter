package com.damdamdeo.formula.autosuggest;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;

public interface LexerFactory {

    Lexer createLexer(CharStream input);
}
