package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.Range;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Range", required = true, requiredProperties = {"start", "end"})
public record RangeDTO(@Schema(required = true) Integer start,
                       @Schema(required = true) Integer end) {
    public RangeDTO(final Range range) {
        this(range.start(), range.end());
    }
}
