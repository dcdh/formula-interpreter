package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.EvaluationProcessedIn;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(name = "EvaluationProcessedInDTO", required = true, requiredProperties = {"evaluatedAtStart", "evaluatedAtEnd", "processedInNanos"})
public record EvaluationProcessedInDTO(@Schema(required = true) ZonedDateTime evaluatedAtStart,
                                       @Schema(required = true) ZonedDateTime evaluatedAtEnd,
                                       @Schema(required = true) long processedInNanos) {
    public EvaluationProcessedInDTO(final EvaluationProcessedIn evaluationProcessedIn) {
        this(
                evaluationProcessedIn.evaluatedAtStart().at(),
                evaluationProcessedIn.evaluatedAtEnd().at(),
                evaluationProcessedIn.in().toNanos()
        );
    }
}

