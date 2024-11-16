package com.damdamdeo.formula.domain.spi;

import com.damdamdeo.formula.domain.ProcessedAt;

public interface ProcessedAtProvider {
    ProcessedAt now();
}
