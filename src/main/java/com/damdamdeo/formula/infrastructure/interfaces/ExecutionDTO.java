package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.Execution;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public record ExecutionDTO(ZonedDateTime executedAtStart,
                           ZonedDateTime executedAtEnd,
                           PositionDTO position,
                           Map<String, String> inputs,
                           String result) {

    public ExecutionDTO(final Execution execution) {
        this(
                execution.executedAtStart().at(),
                execution.executedAtEnd().at(),
                new PositionDTO(execution.position()),
                execution.inputs().entrySet().stream()
                        .collect(Collectors.toMap(
                                entry -> entry.getKey().name(),
                                entry -> entry.getValue().value())),
                execution.result().value()
        );
    }
}
