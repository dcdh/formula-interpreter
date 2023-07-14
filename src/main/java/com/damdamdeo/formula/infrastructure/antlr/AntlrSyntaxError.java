package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.SyntaxError;

public record AntlrSyntaxError(int line, int charPositionInLine, String msg) implements SyntaxError {
}
