package com.damdamdeo.formula.domain;

import java.util.Map;

public interface Execution {

    ExecutionId executionId();

    ExecutedAt executedAt();

    Integer start();

    Integer end();

    Map<InputName, Input> inputs();

    Result result();

}
