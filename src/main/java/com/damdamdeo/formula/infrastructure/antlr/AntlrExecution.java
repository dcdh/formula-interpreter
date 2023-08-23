package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record AntlrExecution(ExecutedAtStart executedAtStart,
                             ExecutedAtEnd executedAtEnd,
                             Position position,
                             Map<InputName, Input> inputs,
                             Result result) implements Execution {
    public AntlrExecution {
        Objects.requireNonNull(executedAtStart);
        Objects.requireNonNull(executedAtEnd);
        Objects.requireNonNull(position);
        Objects.requireNonNull(inputs);
        Objects.requireNonNull(result);
    }

    public static final class Builder {

        ExecutedAtStart executedAtStart;
        ExecutedAtEnd executedAtEnd;
        Position position;
        Map<InputName, Input> inputs = new HashMap<>();
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

        public Builder appendInput(final InputName inputName, final Input value) {
            inputs.put(inputName, value);
            return this;
        }

        public Builder result(final Result result) {
            this.result = result;
            return this;
        }

        public AntlrExecution build() {
            return new AntlrExecution(
                    executedAtStart, executedAtEnd, position, inputs, result
            );
        }
    }

}
