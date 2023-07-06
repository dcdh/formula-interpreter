package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.infrastructure.antlr.SyntaxErrorException;

public interface Executor {
    ExecutionResult execute(Formula formula, StructuredData structuredData) throws SyntaxErrorException;
}
