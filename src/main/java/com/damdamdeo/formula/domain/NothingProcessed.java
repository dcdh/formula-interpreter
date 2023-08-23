package com.damdamdeo.formula.domain;

public record NothingProcessed() implements ProcessedIn {
    @Override
    public long inNanos() {
        return 0;
    }
}
