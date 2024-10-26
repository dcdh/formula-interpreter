package com.damdamdeo.formula.domain;

import java.util.List;

public interface PartEvaluationListener {

    void onBeforePartEvaluation(PartEvaluationId partEvaluationId);

    void onAfterPartEvaluation(PartEvaluationId partEvaluationId, Evaluated evaluated);

    default List<IntermediateResult> intermediateResults() {
        return List.of();
    }
}
