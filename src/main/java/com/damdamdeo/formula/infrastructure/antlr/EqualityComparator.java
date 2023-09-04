package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.NumericalContext;
import com.damdamdeo.formula.domain.Value;

public enum EqualityComparator {

    EQ {
        @Override
        public Value execute(final Value left, final Value right, final NumericalContext numericalContext) {
            return left.equalTo(right, numericalContext);
        }
    },

    NEQ {
        @Override
        public Value execute(final Value left, final Value right, final NumericalContext numericalContext) {
            return left.notEqualTo(right, numericalContext);
        }
    };

    public abstract Value execute(Value left, Value right, NumericalContext numericalContext);

}
