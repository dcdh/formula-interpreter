package com.damdamdeo.formula.domain.spi;

import com.damdamdeo.formula.domain.ExecutedAt;

public interface ExecutedAtProvider {
    ExecutedAt now();
}
