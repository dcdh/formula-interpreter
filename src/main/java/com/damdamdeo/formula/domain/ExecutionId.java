package com.damdamdeo.formula.domain;

import java.util.Objects;
import java.util.UUID;

public record ExecutionId(UUID id) {
    public ExecutionId {
        Objects.requireNonNull(id);
    }
}
