package com.damdamdeo.formula.domain;

import io.smallrye.mutiny.Uni;

public interface Executor {
    Uni<ExecutionResult> execute(Formula formula, StructuredData structuredData, DebugFeature debugFeature) throws ExecutionException;
}
