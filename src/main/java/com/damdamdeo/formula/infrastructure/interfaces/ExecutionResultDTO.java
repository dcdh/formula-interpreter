package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.ExecutionResult;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ExecutionResultDTO(ZonedDateTime executedAtStart,
                                 ZonedDateTime executedAtEnd,
                                 long processedInNanos,
                                 String result,
                                 List<ElementExecutionDTO> elementExecutions) {
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
