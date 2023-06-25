package com.damdamdeo.formula;

import java.math.RoundingMode;

public record NumericalContext(int scale, RoundingMode roundingMode) {
    public NumericalContext() {
        this(6, RoundingMode.HALF_DOWN);
    }
}
