package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.ExecutionResult;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "ExecutionResult", required = true, requiredProperties = {"executedAtStart", "executedAtEnd", "exactProcessedInNanos", "value", "result", "elementExecutions"})
public record ExecutionResultDTO(@Schema(required = true) ZonedDateTime executedAtStart,
                                 @Schema(required = true) ZonedDateTime executedAtEnd,
                                 @Schema(required = true) long exactProcessedInNanos,
                                 @Schema(required = true) String result,
                                 @Schema ParserExecutionProcessedInDTO parserExecutionProcessedIn,
                                 @Schema(required = true) List<ElementExecutionDTO> elementExecutions) {
    public ExecutionResultDTO(final ExecutionResult executionResult) {
        this(
                executionResult.executionProcessedIn().executedAtStart().at(),
                executionResult.executionProcessedIn().executedAtEnd().at(),
                executionResult.exactProcessedInNanos(),
                executionResult.value(),
                executionResult.parserExecutionProcessedIn() != null ? new ParserExecutionProcessedInDTO(executionResult.parserExecutionProcessedIn()) : null,
                executionResult.elementExecutions().stream()
                        .map(ElementExecutionDTO::new)
                        .collect(Collectors.toList()));
    }
}
