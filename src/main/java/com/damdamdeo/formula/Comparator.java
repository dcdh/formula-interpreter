package com.damdamdeo.formula;

import com.damdamdeo.formula.structuredreference.Value;

public enum Comparator {

    GT {
        @Override
        public Value execute(final Value left, final Value right, final NumericalContext numericalContext) {
            return left.greaterThan(right, numericalContext);
        }
    },
    GTE {
        @Override
        public Value execute(final Value left, final Value right, final NumericalContext numericalContext) {
            return left.greaterThanOrEqualTo(right, numericalContext);
        }
    },
    EQ {
        @Override
        public Value execute(final Value left, final Value right, final NumericalContext numericalContext) {
            return left.equalTo(right, numericalContext);
        }
    },
    LT {
        @Override
        public Value execute(final Value left, final Value right, final NumericalContext numericalContext) {
            return left.lessThan(right, numericalContext);
        }
    };


    public abstract Value execute(com.damdamdeo.formula.structuredreference.Value left, Value right, NumericalContext numericalContext);

}
