package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.Position;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Position", required = true, requiredProperties = {"start", "end"})
public record PositionDTO(@Schema(required = true) Integer start,
                          @Schema(required = true) Integer end) {
    public PositionDTO(final Position position) {
        this(position.start(), position.end());
    }
}
