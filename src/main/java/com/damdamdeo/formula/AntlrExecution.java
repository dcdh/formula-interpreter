package com.damdamdeo.formula;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record AntlrExecution(ExecutionId executionId, Integer start, Integer end, Map<InputName, Input> inputs,
                             Result result) implements Execution {
    public AntlrExecution {
        Objects.requireNonNull(executionId);
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        Objects.requireNonNull(inputs);
        Objects.requireNonNull(result);
    }

    public static final class Builder {

        ExecutionId executionId;
        Integer start;
        Integer end;
        Map<InputName, Input> inputs = new HashMap<>();
        Result result;

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder using(final ParserRuleContext parserRuleContext) {
            this.start = parserRuleContext.getStart().getStartIndex();
            this.end = parserRuleContext.getStop().getStopIndex();
            return this;
        }

        public Builder executionId(final ExecutionId executionId) {
            this.executionId = executionId;
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
                    executionId, start, end, inputs, result
            );
        }
    }

    @Override
    public String toString() {
        return "AntlrExecution{" +
                "executionId=" + executionId +
                ", start=" + start +
                ", end=" + end +
                ", inputs=" + inputs +
                ", result=" + result +
                '}';
    }
}
