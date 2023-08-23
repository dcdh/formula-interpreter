package com.damdamdeo.formula.domain;

import java.util.Map;

public interface Execution extends Comparable<Execution> {

    ExecutedAtStart executedAtStart();

    ExecutedAtEnd executedAtEnd();

    Position position();

    Map<InputName, Input> inputs();

    Result result();

    default int compareTo(final Execution execution) {
        return executedAtStart().compareTo(execution.executedAtStart());
    }
}
