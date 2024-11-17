package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.EvaluationLoadingProcessedIn;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(name = "EvaluationLoadingProcessedIn", required = true, requiredProperties = {"processedAtStart", "processedAtEnd", "processedInNanos"})
public record EvaluationLoadingProcessedInDTO(ZonedDateTime processedAtStart, ZonedDateTime processedAtEnd, long processedInNanos) {
    public EvaluationLoadingProcessedInDTO(final EvaluationLoadingProcessedIn evaluationLoadingProcessedIn) {
        this(
                evaluationLoadingProcessedIn.processedAtStart().at(),
                evaluationLoadingProcessedIn.processedAtEnd().at(),
                evaluationLoadingProcessedIn.in().toNanos()
        );
    }
}
