package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.ParserExecutionProcessedIn;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(name = "ParserExecutionProcessedIn", required = true, requiredProperties = {"executedAtStart", "executedAtEnd", "processedInNanos"})
public record ParserExecutionProcessedInDTO(@Schema(required = true) ZonedDateTime executedAtStart,
                                            @Schema(required = true) ZonedDateTime executedAtEnd,
                                            @Schema(required = true) long processedInNanos) {
    public ParserExecutionProcessedInDTO(final ParserExecutionProcessedIn parserExecutionProcessedIn) {
        this(
                parserExecutionProcessedIn.executedAtStart().at(),
                parserExecutionProcessedIn.executedAtEnd().at(),
                parserExecutionProcessedIn.in().toNanos()
        );
    }
}
