package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.NumericalContext;
import com.damdamdeo.formula.domain.Value;

public enum Operator {

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
    },
    DIV {
        @Override
        public Value execute(final Value left, final Value right, final NumericalContext numericalContext) {
            return left.divide(right, numericalContext);
        }
    },
    MUL {
        @Override
        public Value execute(final Value left, final Value right, final NumericalContext numericalContext) {
            return left.multiply(right, numericalContext);
        }
    };

    public abstract Value execute(Value left, Value right, NumericalContext numericalContext);
}
