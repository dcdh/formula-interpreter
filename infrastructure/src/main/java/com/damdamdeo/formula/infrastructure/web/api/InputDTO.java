package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.Input;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Input", required = true, requiredProperties = {"name", "value", "positionedAt"})
public record InputDTO(String name, String value, PositionedAtDTO positionedAt) {

    public InputDTO(final Input input) {
        this(input.name().name(),
                input.value().value(),
                new PositionedAtDTO(input.positionedAt()));
    }
}
