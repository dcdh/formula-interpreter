package com.damdamdeo.formula.domain;

import java.util.Objects;

public final class LogicalBooleanFunction {
    private final Function function;

    private LogicalBooleanFunction(final Function function) {
        this.function = Objects.requireNonNull(function);
    }

    public Value execute(final Value left, final Value right) {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        if (left.isError()) {
            return left;
        } else if (right.isError()) {
            return right;
        } else if (!left.isLogical() || !right.isLogical()) {
            return Value.ofLogicalValueExpected();
        } else {
            return function.execute(left, right);
        }
    }

    public static LogicalBooleanFunction ofAnd() {
        return new LogicalBooleanFunction(Function.AND);
    }

    public static LogicalBooleanFunction ofOr() {
        return new LogicalBooleanFunction(Function.OR);
    }

    private enum Function {

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
}
