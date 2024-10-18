package com.damdamdeo.formula.domain.spi;

import com.damdamdeo.formula.domain.EvaluatedAt;

public interface EvaluatedAtProvider {
    EvaluatedAt now();
}
