package com.damdamdeo.formula.domain.spi;

import com.damdamdeo.formula.domain.Value;

@FunctionalInterface
public interface ValueProvider {
    Value provide();
}
