package com.damdamdeo.formula.infrastructure.antlr.autosuggest;

import org.antlr.v4.runtime.Token;

import java.util.List;

public record TokenizationResult(List<? extends Token> tokens, String untokenizedText) {
}
