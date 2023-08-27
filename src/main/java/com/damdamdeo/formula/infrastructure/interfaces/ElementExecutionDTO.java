package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.ElementExecution;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public record ElementExecutionDTO(ZonedDateTime executedAtStart,
                                  ZonedDateTime executedAtEnd,
                                  long processedInNanos,
                                  PositionDTO position,
                                  Map<String, String> inputs,
                                  String result) {

    public ElementExecutionDTO(final ElementExecution elementExecution) {
        this(
                elementExecution.executionProcessedIn().executedAtStart().at(),
                elementExecution.executionProcessedIn().executedAtEnd().at(),
                elementExecution.executionProcessedIn().in().toNanos(),
                new PositionDTO(elementExecution.position()),
                elementExecution.inputs().entrySet().stream()
                        .collect(Collectors.toMap(
                                entry -> entry.getKey().name(),
                                entry -> entry.getValue().value())),
                elementExecution.result().value()
        );
    }
}
