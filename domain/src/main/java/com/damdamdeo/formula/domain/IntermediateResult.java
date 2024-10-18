package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;

public record IntermediateResult(Value value,
                                 Range range,
                                 List<Input> inputs,
                                 ExecutionProcessedIn executionProcessedIn) {
    public IntermediateResult {
        Objects.requireNonNull(value);
        Objects.requireNonNull(range);
        Objects.requireNonNull(inputs);
        Objects.requireNonNull(executionProcessedIn);
    }

    public static final class Builder {

        ExecutedAtStart executedAtStart;
        ExecutedAtEnd executedAtEnd;
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

        public IntermediateResult.Builder withExecutedAtStart(final ExecutedAtStart executedAtStart) {
            this.executedAtStart = executedAtStart;
            return this;
        }

        public IntermediateResult.Builder withExecutedAtEnd(final ExecutedAtEnd executedAtEnd) {
            this.executedAtEnd = executedAtEnd;
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
                    value, range, inputs, new ExecutionProcessedIn(executedAtStart, executedAtEnd)
            );
        }
    }
}
