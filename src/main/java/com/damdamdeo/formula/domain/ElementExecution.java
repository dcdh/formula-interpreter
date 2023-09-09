package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Objects;

public record ElementExecution(Value value,
                               Range range,
                               List<Input> inputs,
                               ExecutionProcessedIn executionProcessedIn) {
    public ElementExecution {
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

        public static ElementExecution.Builder newBuilder() {
            return new ElementExecution.Builder();
        }

        public ElementExecution.Builder withPosition(final Range range) {
            this.range = range;
            return this;
        }

        public ElementExecution.Builder withExecutedAtStart(final ExecutedAtStart executedAtStart) {
            this.executedAtStart = executedAtStart;
            return this;
        }

        public ElementExecution.Builder withExecutedAtEnd(final ExecutedAtEnd executedAtEnd) {
            this.executedAtEnd = executedAtEnd;
            return this;
        }

        public ElementExecution.Builder withInputs(final List<Input> inputs) {
            this.inputs = inputs;
            return this;
        }

        public ElementExecution.Builder withValue(final Value value) {
            this.value = value;
            return this;
        }

        public ElementExecution build() {
            return new ElementExecution(
                    value, range, inputs, new ExecutionProcessedIn(executedAtStart, executedAtEnd)
            );
        }
    }
}
