package com.damdamdeo.formula.domain.spi;

import com.damdamdeo.formula.domain.EvaluationResult;
import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.PartEvaluationCallback;
import io.smallrye.mutiny.Uni;

public interface Parser {
    @Deprecated
    // Nice example of what has not to be done : PartExecutionCallback depends too much on infrastructure
    // Moreover, no test on domain regarding callback method design while full testing on infra ...
    Uni<EvaluationResult> process(Formula formula, PartEvaluationCallback partEvaluationCallback);

    // TODO create a method doing mapping exclusively
    // The formula need to be evaluated from domain side

}
