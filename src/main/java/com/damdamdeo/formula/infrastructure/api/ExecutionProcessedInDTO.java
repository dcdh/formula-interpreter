package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.ExecutionProcessedIn;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(name = "ExecutionProcessedIn", required = true, requiredProperties = {"executedAtStart", "executedAtEnd", "processedInNanos"})
public record ExecutionProcessedInDTO(@Schema(required = true) ZonedDateTime executedAtStart,
                                      @Schema(required = true) ZonedDateTime executedAtEnd,
                                      @Schema(required = true) long processedInNanos) {
    public ExecutionProcessedInDTO(final ExecutionProcessedIn executionProcessedIn) {
        this(
                executionProcessedIn.executedAtStart().at(),
                executionProcessedIn.executedAtEnd().at(),
                executionProcessedIn.in().toNanos()
        );
    }
}

