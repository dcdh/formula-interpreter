package com.damdamdeo.formula.domain.spi;

import com.damdamdeo.formula.domain.EvaluateOn;
import com.damdamdeo.formula.domain.Formula;

import java.util.Objects;
import java.util.function.Function;

public interface CacheRepository {
    <V> V get(FormulaCacheKey formulaCacheKey, Function<FormulaCacheKey, V> valueLoader);

    record FormulaCacheKey(Formula formula, EvaluateOn evaluateOn) {
        public FormulaCacheKey {
            Objects.requireNonNull(formula);
            Objects.requireNonNull(evaluateOn);
        }
    }
}
