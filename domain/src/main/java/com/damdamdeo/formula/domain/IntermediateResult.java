package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;

public record IntermediateResult(Value value,
                                 PositionedAt positionedAt,
                                 List<Input> inputs,
                                 EvaluationProcessedIn evaluationProcessedIn) {
    public IntermediateResult {
        Objects.requireNonNull(value);
        Objects.requireNonNull(positionedAt);
        Objects.requireNonNull(inputs);
        Objects.requireNonNull(evaluationProcessedIn);
    }

    public static final class Builder {

        ProcessedAtStart processedAtStart;
        ProcessedAtEnd processedAtEnd;
        PositionedAt positionedAt;
        List<Input> inputs;
        Value value;

        public static IntermediateResult.Builder newBuilder() {
            return new IntermediateResult.Builder();
        }

        public IntermediateResult.Builder withPositionedAt(final PositionedAt positionedAt) {
            this.positionedAt = positionedAt;
            return this;
        }

        public IntermediateResult.Builder withEvaluatedAtStart(final ProcessedAtStart processedAtStart) {
            this.processedAtStart = processedAtStart;
            return this;
        }

        public IntermediateResult.Builder withEvaluatedAtEnd(final ProcessedAtEnd processedAtEnd) {
            this.processedAtEnd = processedAtEnd;
            return this;
        }

        public IntermediateResult.Builder withInputs(final List<Input> inputs) {
            this.inputs = inputs;
            return this;
        }

        public IntermediateResult.Builder withValue(final Value value) {
            this.value = value;
            return this;
        }

        public IntermediateResult build() {
            return new IntermediateResult(
                    value, positionedAt, inputs, new EvaluationProcessedIn(processedAtStart, processedAtEnd)
            );
        }
    }
}
