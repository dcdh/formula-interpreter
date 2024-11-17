package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.FormulaCacheRetrieval;
import com.damdamdeo.formula.domain.ProcessingMetrics;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "EvaluationResult", required = true, requiredProperties = {"exactProcessedInNanos",
        "formulaCacheRetrieval", "parserEvaluationProcessedIn", "evaluationProcessedIn"})
public record ProcessingMetricsDTO(long exactProcessedInNanos,
                                   FormulaCacheRetrieval formulaCacheRetrieval,
                                   EvaluationLoadingProcessedInDTO parserEvaluationProcessedIn,
                                   EvaluationProcessedInDTO evaluationProcessedIn) {
    public ProcessingMetricsDTO(final ProcessingMetrics processingMetrics) {
        this(
                processingMetrics.exactProcessedInNanos(),
                processingMetrics.formulaCacheRetrieval(),
                new EvaluationLoadingProcessedInDTO(processingMetrics.evaluationLoadingProcessedIn()),
                new EvaluationProcessedInDTO(processingMetrics.evaluationProcessedIn())
        );
    }
}
