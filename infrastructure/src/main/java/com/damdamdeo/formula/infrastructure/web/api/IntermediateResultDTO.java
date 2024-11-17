package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.IntermediateResult;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "IntermediateResult", required = true, requiredProperties = {"evaluatedAtStart", "evaluatedAtEnd", "processedInNanos", "positionedAt", "inputs", "value"})
public record IntermediateResultDTO(ZonedDateTime evaluatedAtStart, ZonedDateTime evaluatedAtEnd,
                                    long processedInNanos, PositionedAtDTO positionedAt,
                                    List<InputDTO> inputs, String result) {

    public IntermediateResultDTO(final IntermediateResult intermediateResult) {
        this(
                intermediateResult.evaluationProcessedIn().processedAtStart().at(),
                intermediateResult.evaluationProcessedIn().processedAtEnd().at(),
                intermediateResult.evaluationProcessedIn().in().toNanos(),
                new PositionedAtDTO(intermediateResult.positionedAt()),
                intermediateResult.inputs().stream()
                        .map(InputDTO::new).collect(Collectors.toList()),
                intermediateResult.value().value()
        );
    }
}
