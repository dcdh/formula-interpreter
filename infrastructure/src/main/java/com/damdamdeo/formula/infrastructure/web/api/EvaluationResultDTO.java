package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.EvaluationResult;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "EvaluationResult", required = true, requiredProperties = {"result", "processingMetrics", "intermediateResults"})
public record EvaluationResultDTO(@Schema(required = true) String result,
                                  @Schema(required = true) ProcessingMetricsDTO processingMetrics,
                                  @Schema(required = true) List<IntermediateResultDTO> intermediateResults) {
    public EvaluationResultDTO(final EvaluationResult evaluationResult) {
        this(
                evaluationResult.value().value(),
                new ProcessingMetricsDTO(evaluationResult.processingMetrics()),
                evaluationResult.intermediateResults().stream()
                        .map(IntermediateResultDTO::new)
                        .collect(Collectors.toList()));
    }
}
