package com.damdamdeo.formula.domain;

public final class NoOpPartEvaluationListener implements PartEvaluationListener {

    @Override
    public void onBeforePartEvaluation(final PartEvaluationId partEvaluationId) {
    }

    @Override
    public void onAfterPartEvaluation(final PartEvaluationId partEvaluationId, final Evaluated evaluated) {
    }
}
