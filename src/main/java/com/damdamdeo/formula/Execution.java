package com.damdamdeo.formula;

import java.util.Map;

public interface Execution {

    ExecutionId executionId();
    Integer start();

    Integer end();

    Map<InputName, Input> inputs();

    Result result();

}
