package com.damdamdeo.formula.domain;

public interface Executor {
    ExecutionResult execute(Formula formula, StructuredData structuredData) throws SyntaxErrorException;
}
