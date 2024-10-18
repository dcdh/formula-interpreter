package com.damdamdeo.formula.domain;

public final class NoOpPartExecutionCallbackListener implements PartExecutionCallbackListener {

    @Override
    public void onBeforeExecution(final ExecutionId executionId) {
    }

    @Override
    public void onAfterExecution(final ExecutionId executionId, final Result result) {
    }
}
