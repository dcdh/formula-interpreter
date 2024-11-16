package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.FormulaCacheRetrieval;
import com.damdamdeo.formula.domain.ProcessingMetrics;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "EvaluationResult", required = true, requiredProperties = {"exactProcessedInNanos",
        "formulaCacheRetrieval", "parserEvaluationProcessedIn", "evaluationProcessedIn"})
public record ProcessingMetricsDTO(@Schema(required = true) long exactProcessedInNanos,
                                   @Schema(required = true) FormulaCacheRetrieval formulaCacheRetrieval,
                                   @Schema(required = true) EvaluationLoadingProcessedInDTO parserEvaluationProcessedIn,
                                   @Schema(required = true) EvaluationProcessedInDTO evaluationProcessedIn) {
    public ProcessingMetricsDTO(final ProcessingMetrics processingMetrics) {
        this(
                processingMetrics.exactProcessedInNanos(),
                processingMetrics.formulaCacheRetrieval(),
                new EvaluationLoadingProcessedInDTO(processingMetrics.evaluationLoadingProcessedIn()),
                new EvaluationProcessedInDTO(processingMetrics.evaluationProcessedIn())
        );
    }
}
