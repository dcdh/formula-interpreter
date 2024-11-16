package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.spi.ProcessedAtProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class DebugPartEvaluationListener implements PartEvaluationListener {
    private final ProcessedAtProvider processedAtProvider;
    private final Map<PartEvaluationId, IntermediateResult> intermediateResultsByExecutionId;
    private final Map<PartEvaluationId, ProcessedAtStart> evaluatedAtStartsByExecutionId;

    public DebugPartEvaluationListener(final ProcessedAtProvider processedAtProvider) {
        this.processedAtProvider = Objects.requireNonNull(processedAtProvider);
        this.intermediateResultsByExecutionId = new HashMap<>();
        this.evaluatedAtStartsByExecutionId = new HashMap<>();
    }

    @Override
    public void onBeforePartEvaluation(final PartEvaluationId partEvaluationId) {
        evaluatedAtStartsByExecutionId.put(partEvaluationId, processedAtProvider.now());
    }

    @Override
    public void onAfterPartEvaluation(final PartEvaluationId partEvaluationId, final Evaluated evaluated) {
        intermediateResultsByExecutionId.put(partEvaluationId,
                IntermediateResult.Builder.newBuilder()
                        .withValue(evaluated.value())
                        .withInputs(evaluated.inputs().get())
                        .withPositionedAt(evaluated.positionedAt())
                        .withEvaluatedAtStart(evaluatedAtStartsByExecutionId.get(partEvaluationId))
                        .withEvaluatedAtEnd(processedAtProvider.now())
                        .build());
    }

    public List<IntermediateResult> intermediateResults() {
        return intermediateResultsByExecutionId
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();
    }

}
