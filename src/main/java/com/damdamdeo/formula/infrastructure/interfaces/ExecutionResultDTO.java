package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.ExecutionResult;

import java.util.List;
import java.util.stream.Collectors;

public record ExecutionResultDTO(String result,
                                 long processedInNanos,
                                 List<ExecutionDTO> executions) {
    public ExecutionResultDTO(final ExecutionResult executionResult) {
        this(executionResult.result().value(),
                executionResult.processedIn().inNanos(),
                executionResult.executions().stream()
                        .map(ExecutionDTO::new)
                        .collect(Collectors.toList()));
    }
}
