package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.ExecutionResult;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ExecutionResultDTO(@Schema(required = true) ZonedDateTime executedAtStart,
                                 @Schema(required = true) ZonedDateTime executedAtEnd,
                                 @Schema(required = true) long processedInNanos,
                                 @Schema(required = true) String result,
                                 @Schema(required = true) List<ElementExecutionDTO> elementExecutions) {
    public ExecutionResultDTO(final ExecutionResult executionResult) {
        this(
                executionResult.executionProcessedIn().executedAtStart().at(),
                executionResult.executionProcessedIn().executedAtEnd().at(),
                executionResult.executionProcessedIn().in().toNanos(),
                executionResult.result().value(),
                executionResult.elementExecutions().stream()
                        .map(ElementExecutionDTO::new)
                        .collect(Collectors.toList()));
    }
}
