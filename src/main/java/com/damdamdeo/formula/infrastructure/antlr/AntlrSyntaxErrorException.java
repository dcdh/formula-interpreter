package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.SyntaxErrorException;

public final class AntlrSyntaxErrorException extends SyntaxErrorException {
    public AntlrSyntaxErrorException(final Formula formula, final AntlrSyntaxError syntaxError) {
        super(formula, syntaxError);
    }
}
