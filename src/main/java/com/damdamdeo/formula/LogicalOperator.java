package com.damdamdeo.formula;

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
