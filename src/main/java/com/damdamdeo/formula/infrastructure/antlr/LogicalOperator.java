package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.domain.Value;

public enum LogicalOperator {

    OR {
        @Override
        public Value execute(final Value left, final Value right) {
            return left.or(right);
        }
    },
    AND {
        @Override
        public Value execute(final Value left, final Value right) {
            return left.and(right);
        }
    };

    public abstract Value execute(Value left, Value right);
}
