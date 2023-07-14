package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxError;

public record SyntaxErrorDTO(Integer line, Integer charPositionInLine, String msg) {
    public SyntaxErrorDTO(final AntlrSyntaxError antlrSyntaxError) {
        this(antlrSyntaxError.line(),
                antlrSyntaxError.charPositionInLine(),
                antlrSyntaxError.msg());
    }
}
