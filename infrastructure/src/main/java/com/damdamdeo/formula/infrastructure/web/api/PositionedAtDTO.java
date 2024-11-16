package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.PositionedAt;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "PositionedAt", required = true, requiredProperties = {"start", "end"})
public record PositionedAtDTO(@Schema(required = true) Integer start,
                              @Schema(required = true) Integer end) {
    public PositionedAtDTO(final PositionedAt positionedAt) {
        this(positionedAt.positionStart().start(), positionedAt.positionEnd().end());
    }
}
