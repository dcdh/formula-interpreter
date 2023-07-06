package com.damdamdeo.formula.domain;

import java.util.List;

public interface ExecutionLogger {
    void log(Execution execution);

    List<Execution> getByExecutionId(ExecutionId executionId);
}
