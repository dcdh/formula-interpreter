package com.damdamdeo.formula.infrastructure.antlr;

public record SyntaxError(int line, int charPositionInLine, String msg) {
}
