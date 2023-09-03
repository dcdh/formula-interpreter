package com.damdamdeo.formula.domain;

import java.util.List;
import java.util.Map;

public interface ElementExecution {
    List<Position> positions();

    Map<InputName, Input> inputs();

    Result result();

    ExecutionProcessedIn executionProcessedIn();
}
