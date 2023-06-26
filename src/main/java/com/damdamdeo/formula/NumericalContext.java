package com.damdamdeo.formula;

import java.math.RoundingMode;

public record NumericalContext(int precision, int scale, RoundingMode roundingMode) {
    public NumericalContext() {
        this(20, 6, RoundingMode.HALF_DOWN);
    }
}
