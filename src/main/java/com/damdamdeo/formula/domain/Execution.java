package com.damdamdeo.formula.domain;

import java.util.Map;

public interface Execution {
    ExecutedAtStart executedAtStart();

    ExecutedAtEnd executedAtEnd();

    Position position();

    Map<InputName, Input> inputs();

    Result result();
}
