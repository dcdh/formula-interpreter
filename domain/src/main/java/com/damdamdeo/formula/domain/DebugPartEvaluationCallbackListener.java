package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.spi.EvaluatedAtProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class DebugPartEvaluationCallbackListener implements PartEvaluationCallbackListener {
    private final EvaluatedAtProvider evaluatedAtProvider;
    private final Map<PartEvaluationId, IntermediateResult> intermediateResultsByExecutionId;
    private final Map<PartEvaluationId, EvaluatedAtStart> evaluatedAtStartsByExecutionId;

    public DebugPartEvaluationCallbackListener(final EvaluatedAtProvider evaluatedAtProvider) {
        this.evaluatedAtProvider = Objects.requireNonNull(evaluatedAtProvider);
        this.intermediateResultsByExecutionId = new HashMap<>();
        this.evaluatedAtStartsByExecutionId = new HashMap<>();
    }

    @Override
    public void onBeforePartEvaluation(final PartEvaluationId partEvaluationId) {
        evaluatedAtStartsByExecutionId.put(partEvaluationId, evaluatedAtProvider.now());
    }

    @Override
    public void onAfterPartEvaluation(final PartEvaluationId partEvaluationId, final Evaluated evaluated) {
        intermediateResultsByExecutionId.put(partEvaluationId,
                IntermediateResult.Builder.newBuilder()
                        .withValue(evaluated.value())
                        .withInputs(evaluated.inputs().get())
                        .withRange(evaluated.range())
                        .withEvaluatedAtStart(evaluatedAtStartsByExecutionId.get(partEvaluationId))
                        .withEvaluatedAtEnd(evaluatedAtProvider.now())
                        .build());
    }

    @Override
    public List<IntermediateResult> intermediateResults() {
        return intermediateResultsByExecutionId
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();
    }

}
