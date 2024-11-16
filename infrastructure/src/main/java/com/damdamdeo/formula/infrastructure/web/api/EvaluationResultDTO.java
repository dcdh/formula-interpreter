package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.EvaluationResult;
import com.damdamdeo.formula.domain.FormulaCacheRetrieval;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "EvaluationResult", required = true, requiredProperties = {"result", "exactProcessedInNanos", "formulaCacheRetrieval",
        "parserEvaluationProcessedIn",
        "evaluationProcessedIn", "intermediateResults"})
public record EvaluationResultDTO(@Schema(required = true) String result,
                                  @Schema(required = true) long exactProcessedInNanos,
                                  @Schema(required = true) FormulaCacheRetrieval formulaCacheRetrieval,
                                  @Schema(required = true) EvaluationLoadingProcessedInDTO parserEvaluationProcessedIn,
                                  @Schema(required = true) EvaluationProcessedInDTO evaluationProcessedIn,
                                  @Schema(required = true) List<IntermediateResultDTO> intermediateResults) {
    public EvaluationResultDTO(final EvaluationResult evaluationResult) {
        this(
                evaluationResult.value().value(),
                evaluationResult.exactProcessedInNanos(),
                evaluationResult.formulaCacheRetrieval(),
                evaluationResult.evaluationLoadingProcessedIn() != null ? new EvaluationLoadingProcessedInDTO(evaluationResult.evaluationLoadingProcessedIn()) : null,
                new EvaluationProcessedInDTO(evaluationResult.evaluationProcessedIn()),
                evaluationResult.intermediateResults().stream()
                        .map(IntermediateResultDTO::new)
                        .collect(Collectors.toList()));
    }
}
