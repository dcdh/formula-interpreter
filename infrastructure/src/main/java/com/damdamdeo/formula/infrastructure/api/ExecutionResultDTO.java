package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.ExecutionResult;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "ExecutionResult", required = true, requiredProperties = {"result", "exactProcessedInNanos", "executionProcessedIn", "intermediateResults"})
public record ExecutionResultDTO(@Schema(required = true) String result,
                                 @Schema(required = true) long exactProcessedInNanos,
                                 @Schema ParserExecutionProcessedInDTO parserExecutionProcessedIn,
                                 @Schema(required = true) ExecutionProcessedInDTO executionProcessedIn,
                                 @Schema(required = true) List<IntermediateResultDTO> intermediateResults) {
    public ExecutionResultDTO(final ExecutionResult executionResult) {
        this(
                executionResult.value(),
                executionResult.exactProcessedInNanos(),
                executionResult.parserExecutionProcessedIn() != null ? new ParserExecutionProcessedInDTO(executionResult.parserExecutionProcessedIn()) : null,
                new ExecutionProcessedInDTO(executionResult.executionProcessedIn()),
                executionResult.intermediateResults().stream()
                        .map(IntermediateResultDTO::new)
                        .collect(Collectors.toList()));
    }
}
