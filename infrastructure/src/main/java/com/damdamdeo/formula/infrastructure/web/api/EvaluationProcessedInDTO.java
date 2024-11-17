package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.EvaluationProcessedIn;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(name = "EvaluationProcessedInDTO", required = true, requiredProperties = {"processedAtStart", "processedAtEnd", "processedInNanos"})
public record EvaluationProcessedInDTO(ZonedDateTime processedAtStart, ZonedDateTime processedAtEnd, long processedInNanos) {
    public EvaluationProcessedInDTO(final EvaluationProcessedIn evaluationProcessedIn) {
        this(
                evaluationProcessedIn.processedAtStart().at(),
                evaluationProcessedIn.processedAtEnd().at(),
                evaluationProcessedIn.in().toNanos()
        );
    }
}

