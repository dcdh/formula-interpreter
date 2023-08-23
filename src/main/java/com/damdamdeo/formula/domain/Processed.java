package com.damdamdeo.formula.domain;

import java.time.Duration;
import java.util.List;

public record Processed(List<Execution> executions) implements ProcessedIn {
    public Processed {
        if (executions == null || executions.size() == 0) {
            throw new IllegalStateException("executions must have at least one element");
        }
    }

    @Override
    public long inNanos() {
        final Execution first = executions.get(0);
        final Execution last = executions.get(executions.size() - 1);
        return Duration.between(
                first.executedAtStart().at(),
                last.executedAtEnd().at()).toNanos();
    }

}
