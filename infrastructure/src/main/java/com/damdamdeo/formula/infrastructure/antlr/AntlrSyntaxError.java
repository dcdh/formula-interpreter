package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.SyntaxError;

public record AntlrSyntaxError(int line, int charPositionInLine, String msg) implements SyntaxError {
    @Override
    public String toString() {
        return String.format("Syntax error at line '%d' at positionedAt '%d' with message '%s'", line, charPositionInLine, msg);
    }
}
