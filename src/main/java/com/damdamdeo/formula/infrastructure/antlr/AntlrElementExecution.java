package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public record AntlrElementExecution(List<Position> positions,
                                    Map<InputName, Input> inputs,
                                    Result result,
                                    ExecutionProcessedIn executionProcessedIn) implements ElementExecution {
    public AntlrElementExecution {
        Objects.requireNonNull(positions);
        Objects.requireNonNull(inputs);
        Objects.requireNonNull(result);
        Objects.requireNonNull(executionProcessedIn);
    }

    public static final class Builder {

        ExecutedAtStart executedAtStart;
        ExecutedAtEnd executedAtEnd;
        List<Position> positions;
        Map<InputName, Input> inputs;
        Result result;

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withPositions(final List<Position> positions) {
            this.positions = positions;
            return this;
        }

        public Builder executedAtStart(final ExecutedAtStart executedAtStart) {
            this.executedAtStart = executedAtStart;
            return this;
        }

        public Builder executedAtEnd(final ExecutedAtEnd executedAtEnd) {
            this.executedAtEnd = executedAtEnd;
            return this;
        }

        public Builder withInputs(final Map<InputName, Input> inputs) {
            this.inputs = inputs;
            return this;
        }

        public Builder result(final Result result) {
            this.result = result;
            return this;
        }

        public AntlrElementExecution build() {
            return new AntlrElementExecution(
                    positions, inputs, result, new ExecutionProcessedIn(executedAtStart, executedAtEnd)
            );
        }
    }

}
