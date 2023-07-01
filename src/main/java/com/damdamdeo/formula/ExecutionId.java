package com.damdamdeo.formula;

import java.util.UUID;

public record ExecutionId(UUID id) {
    @Override
    public String toString() {
        return "ExecutionId{" +
                "id=" + id +
                '}';
    }
}
