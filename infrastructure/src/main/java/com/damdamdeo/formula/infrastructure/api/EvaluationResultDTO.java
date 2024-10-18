package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.EvaluationResult;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "EvaluationResult", required = true, requiredProperties = {"result", "exactProcessedInNanos", "evaluationProcessedIn", "intermediateResults"})
public record EvaluationResultDTO(@Schema(required = true) String result,
                                  @Schema(required = true) long exactProcessedInNanos,
                                  @Schema ParserEvaluationProcessedInDTO parserEvaluationProcessedIn,
                                  @Schema(required = true) EvaluationProcessedInDTO evaluationProcessedIn,
                                  @Schema(required = true) List<IntermediateResultDTO> intermediateResults) {
    public EvaluationResultDTO(final EvaluationResult evaluationResult) {
        this(
                evaluationResult.value(),
                evaluationResult.exactProcessedInNanos(),
                evaluationResult.parserEvaluationProcessedIn() != null ? new ParserEvaluationProcessedInDTO(evaluationResult.parserEvaluationProcessedIn()) : null,
                new EvaluationProcessedInDTO(evaluationResult.evaluationProcessedIn()),
                evaluationResult.intermediateResults().stream()
                        .map(IntermediateResultDTO::new)
                        .collect(Collectors.toList()));
    }
}
