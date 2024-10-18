package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.ParserEvaluationProcessedIn;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(name = "ParserEvaluationProcessedIn", required = true, requiredProperties = {"evaluatedAtStart", "evaluatedAtEnd", "processedInNanos"})
public record ParserEvaluationProcessedInDTO(@Schema(required = true) ZonedDateTime evaluatedAtStart,
                                             @Schema(required = true) ZonedDateTime evaluatedAtEnd,
                                             @Schema(required = true) long processedInNanos) {
    public ParserEvaluationProcessedInDTO(final ParserEvaluationProcessedIn parserEvaluationProcessedIn) {
        this(
                parserEvaluationProcessedIn.evaluatedAtStart().at(),
                parserEvaluationProcessedIn.evaluatedAtEnd().at(),
                parserEvaluationProcessedIn.in().toNanos()
        );
    }
}
