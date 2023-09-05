package com.damdamdeo.formula.domain.spi;

import com.damdamdeo.formula.domain.ExecutionResult;
import com.damdamdeo.formula.domain.ExecutionWrapper;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.StructuredData;
import io.smallrye.mutiny.Uni;

public interface Executor {
    Uni<ExecutionResult> execute(Formula formula, StructuredData structuredData, ExecutionWrapper executionWrapper);
}
