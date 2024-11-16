package com.damdamdeo.formula.domain.spi;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.evaluation.Loaded;

import java.util.List;

public interface EvaluationPipeline<T extends Loaded> {
    T load(Formula formula);

    Evaluated evaluate(T loaded, PartEvaluationListener partEvaluationListener, List<StructuredReference> structuredReferences, NumericalContext numericalContext);
}
