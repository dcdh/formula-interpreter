package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.Input;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Input", required = true, requiredProperties = {"name", "value", "positionedAt"})
public record InputDTO(@Schema(required = true) String name,
                       @Schema(required = true) String value,
                       @Schema(required = true) PositionedAtDTO positionedAt) {

    public InputDTO(final Input input) {
        this(input.name().name(),
                input.value().value(),
                new PositionedAtDTO(input.positionedAt()));
    }
}
