package com.damdamdeo.formula.domain;

public final class NoOpPartEvaluationCallbackListener implements PartEvaluationCallbackListener {

    @Override
    public void onBeforePartEvaluation(final PartEvaluationId partEvaluationId) {
    }

    @Override
    public void onAfterPartEvaluation(final PartEvaluationId partEvaluationId, final Evaluated evaluated) {
    }
}
