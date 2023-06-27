package com.damdamdeo.formula;

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
    },
    LTE {
        @Override
        public Value execute(final Value left, final Value right, final NumericalContext numericalContext) {
            return left.lessThanOrEqualTo(right, numericalContext);
        }
    };


    public abstract Value execute(Value left, Value right, NumericalContext numericalContext);

}
