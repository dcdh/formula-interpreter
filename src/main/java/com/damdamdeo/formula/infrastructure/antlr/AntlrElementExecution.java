package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Map;
import java.util.Objects;

public record AntlrElementExecution(Position position,
                                    Map<InputName, Input> inputs,
                                    Result result,
                                    ExecutionProcessedIn executionProcessedIn) implements ElementExecution {
    public AntlrElementExecution {
        Objects.requireNonNull(position);
        Objects.requireNonNull(inputs);
        Objects.requireNonNull(result);
        Objects.requireNonNull(executionProcessedIn);
    }

    public static final class Builder {

        ExecutedAtStart executedAtStart;
        ExecutedAtEnd executedAtEnd;
        Position position;
        Map<InputName, Input> inputs;
        Result result;

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder using(final ParserRuleContext parserRuleContext) {
            this.position = new Position(
                    parserRuleContext.getStart().getStartIndex(),
                    parserRuleContext.getStop().getStopIndex()
            );
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
                    position, inputs, result, new ExecutionProcessedIn(executedAtStart, executedAtEnd)
            );
        }
    }

}
