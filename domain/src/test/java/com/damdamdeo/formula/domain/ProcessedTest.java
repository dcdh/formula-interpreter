package com.damdamdeo.formula.domain;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessedTest {

    @Test
    public void shouldProcessedInOneExecution() {
        // Given
        final EvaluatedAtStart givenEvaluatedAtStart = new EvaluatedAt(ZonedDateTime.parse("2023-08-27T16:00:18.538583565+02:00"));
        final EvaluatedAtEnd givenEvaluatedAtEnd = new EvaluatedAt(ZonedDateTime.parse("2023-08-27T16:00:18.546082832+02:00"));

        // When
        final Duration in = new EvaluationProcessedIn(givenEvaluatedAtStart, givenEvaluatedAtEnd).in();

        // Then
        assertThat(in).hasNanos(7_499_267L);
    }
}