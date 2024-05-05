package com.damdamdeo.formula.domain;

import java.util.Objects;

public record ParserExecutionProcessedIn(ExecutedAtStart executedAtStart,
                                         ExecutedAtEnd executedAtEnd) implements ProcessedIn {
    public ParserExecutionProcessedIn {
        Objects.requireNonNull(executedAtStart);
        Objects.requireNonNull(executedAtEnd);
    }
}
