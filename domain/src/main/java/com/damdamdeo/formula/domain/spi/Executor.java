package com.damdamdeo.formula.domain.spi;

import com.damdamdeo.formula.domain.ExecutionResult;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.PartExecutionCallback;
import io.smallrye.mutiny.Uni;

public interface Executor {
    @Deprecated
    // Nice example of what has not to be done : PartExecutionCallback depends too much on infrastructure
    // Moreover, no test on domain regarding callback method design while full testing on infra ...
    Uni<ExecutionResult> execute(Formula formula, PartExecutionCallback partExecutionCallback);

    // TODO create a method doing mapping exclusively
    // The formula need to be evaluated from domain side

}
