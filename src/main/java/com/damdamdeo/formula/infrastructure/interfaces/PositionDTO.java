package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.Position;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record PositionDTO(@Schema(required = true) Integer start,
                          @Schema(required = true) Integer end) {
    public PositionDTO(final Position position) {
        this(position.start(), position.end());
    }
}
