package com.damdamdeo.formula.domain;

import java.time.Duration;

public interface ProcessedIn {
    ProcessedAtStart processedAtStart();

    ProcessedAtEnd processedAtEnd();

    default Duration in() {
        return Duration.between(
                processedAtStart().at(),
                processedAtEnd().at());
    }
}
