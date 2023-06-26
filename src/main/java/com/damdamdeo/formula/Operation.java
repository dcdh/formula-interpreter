package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.Value;

public enum Operation {

    ADD {
        @Override
        public Value execute(final Value left, final Value right, final NumericalContext numericalContext) {
            return left.add(right, numericalContext);
        }
    },
    SUB {
        @Override
        public Value execute(final Value left, final Value right, final NumericalContext numericalContext) {
            return left.subtract(right, numericalContext);
        }
    };

    public abstract Value execute(Value left, Value right, NumericalContext numericalContext);
}
