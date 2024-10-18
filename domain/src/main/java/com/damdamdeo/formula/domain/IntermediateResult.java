package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;

public record IntermediateResult(Value value,
                                 Range range,
                                 List<Input> inputs,
                                 EvaluationProcessedIn evaluationProcessedIn) {
    public IntermediateResult {
        Objects.requireNonNull(value);
        Objects.requireNonNull(range);
        Objects.requireNonNull(inputs);
        Objects.requireNonNull(evaluationProcessedIn);
    }

    public static final class Builder {

        EvaluatedAtStart evaluatedAtStart;
        EvaluatedAtEnd evaluatedAtEnd;
        Range range;
        List<Input> inputs;
        Value value;

        public static IntermediateResult.Builder newBuilder() {
            return new IntermediateResult.Builder();
        }

        public IntermediateResult.Builder withPosition(final Range range) {
            this.range = range;
            return this;
        }

        public IntermediateResult.Builder withEvaluatedAtStart(final EvaluatedAtStart evaluatedAtStart) {
            this.evaluatedAtStart = evaluatedAtStart;
            return this;
        }

        public IntermediateResult.Builder withEvaluatedAtEnd(final EvaluatedAtEnd evaluatedAtEnd) {
            this.evaluatedAtEnd = evaluatedAtEnd;
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
                    value, range, inputs, new EvaluationProcessedIn(evaluatedAtStart, evaluatedAtEnd)
            );
        }
    }
}
