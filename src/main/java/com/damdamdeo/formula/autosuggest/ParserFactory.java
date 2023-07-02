package com.damdamdeo.formula.autosuggest;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

public interface ParserFactory {

    Parser createParser(TokenStream tokenStream);

}
