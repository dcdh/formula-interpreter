package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.Position;

public record PositionDTO(Integer start, Integer end) {
    public PositionDTO(final Position position) {
        this(position.start(), position.end());
    }
}
