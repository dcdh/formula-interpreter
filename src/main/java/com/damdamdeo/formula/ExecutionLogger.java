package com.damdamdeo.formula;

import java.util.List;

public interface ExecutionLogger {
    void log(Execution execution);

    List<Execution> getByExecutionId(ExecutionId executionId);

}
