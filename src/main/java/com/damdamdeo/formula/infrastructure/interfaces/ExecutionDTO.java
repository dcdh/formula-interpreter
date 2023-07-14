package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.Execution;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record ExecutionDTO(UUID executionId, ZonedDateTime executedAt, Integer start, Integer end,
                           Map<String, String> inputs, String result) {

    public ExecutionDTO(final Execution execution) {
        this(
                execution.executionId().id(),
                execution.executedAt().at(),
                execution.start(),
                execution.end(),
                execution.inputs().entrySet().stream()
                        .collect(Collectors.toMap(
                                entry -> entry.getKey().name(),
                                entry -> entry.getValue().value())),
                execution.result().value()
        );
    }
}
