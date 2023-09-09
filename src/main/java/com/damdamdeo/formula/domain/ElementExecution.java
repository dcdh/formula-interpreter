package com.damdamdeo.formula.domain;

import java.util.Map;
import java.util.Objects;

public record ElementExecution(Value result,
                               Range range,
                               Map<InputName, Input> inputs,
                               ExecutionProcessedIn executionProcessedIn) {
    public ElementExecution {
        Objects.requireNonNull(result);
        Objects.requireNonNull(range);
        Objects.requireNonNull(inputs);
        Objects.requireNonNull(executionProcessedIn);
    }

    public static final class Builder {

        ExecutedAtStart executedAtStart;
        ExecutedAtEnd executedAtEnd;
        Range range;
        Map<InputName, Input> inputs;
        Value result;

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

        public ElementExecution.Builder withInputs(final Map<InputName, Input> inputs) {
            this.inputs = inputs;
            return this;
        }

        public ElementExecution.Builder withResult(final Value result) {
            this.result = result;
            return this;
        }

        public ElementExecution build() {
            return new ElementExecution(
                    result, range, inputs, new ExecutionProcessedIn(executedAtStart, executedAtEnd)
            );
        }
    }
}
