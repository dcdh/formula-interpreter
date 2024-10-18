package com.damdamdeo.formula.domain;

import java.util.List;

public interface PartExecutionCallbackListener {

    void onBeforeExecution(ExecutionId executionId);

    void onAfterExecution(ExecutionId executionId, Result result);

    default List<IntermediateResult> intermediateResults() {
        return List.of();
    }
}
