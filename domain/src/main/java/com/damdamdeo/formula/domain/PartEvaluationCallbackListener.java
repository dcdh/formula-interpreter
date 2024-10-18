package com.damdamdeo.formula.domain;

import java.util.List;

public interface PartEvaluationCallbackListener {

    void onBeforePartEvaluation(PartEvaluationId partEvaluationId);

    void onAfterPartEvaluation(PartEvaluationId partEvaluationId, Result result);

    default List<IntermediateResult> intermediateResults() {
        return List.of();
    }
}
