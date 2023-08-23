package com.damdamdeo.formula.domain;

import java.time.ZonedDateTime;

public interface ExecutedAtStart extends Comparable<ExecutedAtStart> {
    ZonedDateTime at();

    @Override
    default int compareTo(final ExecutedAtStart executedAtStart) {
        return at().compareTo(executedAtStart.at());
    }
}
