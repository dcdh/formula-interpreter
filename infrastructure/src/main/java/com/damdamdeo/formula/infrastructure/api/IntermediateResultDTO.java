package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.IntermediateResult;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "IntermediateResult", required = true, requiredProperties = {"executedAtStart", "executedAtEnd", "processedInNanos", "range", "inputs", "value"})
public record IntermediateResultDTO(@Schema(required = true) ZonedDateTime executedAtStart,
                                    @Schema(required = true) ZonedDateTime executedAtEnd,
                                    @Schema(required = true) long processedInNanos,
                                    @Schema(required = true) RangeDTO range,
                                    @Schema(required = true) List<InputDTO> inputs,
                                    @Schema(required = true) String result) {

    public IntermediateResultDTO(final IntermediateResult intermediateResult) {
        this(
                intermediateResult.executionProcessedIn().executedAtStart().at(),
                intermediateResult.executionProcessedIn().executedAtEnd().at(),
                intermediateResult.executionProcessedIn().in().toNanos(),
                new RangeDTO(intermediateResult.range()),
                intermediateResult.inputs().stream()
                        .map(InputDTO::new).collect(Collectors.toList()),
                intermediateResult.value().value()
        );
    }
}
