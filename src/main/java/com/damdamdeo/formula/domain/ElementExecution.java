package com.damdamdeo.formula.domain;

import java.util.Map;

public interface ElementExecution {
    Position position();

    Map<InputName, Input> inputs();

    Result result();

    ExecutionProcessedIn executionProcessedIn();
}
