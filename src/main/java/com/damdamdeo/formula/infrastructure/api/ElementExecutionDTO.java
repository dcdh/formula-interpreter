package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.ElementExecution;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "ElementExecution", required = true, requiredProperties = {"executedAtStart", "executedAtEnd", "processedInNanos", "range", "inputs", "value"})
public record ElementExecutionDTO(@Schema(required = true) ZonedDateTime executedAtStart,
                                  @Schema(required = true) ZonedDateTime executedAtEnd,
                                  @Schema(required = true) long processedInNanos,
                                  @Schema(required = true) RangeDTO range,
                                  @Schema(required = true) List<InputDTO> inputs,
                                  @Schema(required = true) String result) {

    public ElementExecutionDTO(final ElementExecution elementExecution) {
        this(
                elementExecution.executionProcessedIn().executedAtStart().at(),
                elementExecution.executionProcessedIn().executedAtEnd().at(),
                elementExecution.executionProcessedIn().in().toNanos(),
                new RangeDTO(elementExecution.range()),
                elementExecution.inputs().stream()
                        .map(InputDTO::new).collect(Collectors.toList()),
                elementExecution.value().value()
        );
    }
}
