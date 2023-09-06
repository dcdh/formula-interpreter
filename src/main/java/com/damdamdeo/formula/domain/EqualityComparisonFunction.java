package com.damdamdeo.formula.domain;

import java.util.Objects;

public final class EqualityComparisonFunction implements ComparisonFunction {
    private final Function function;

    private EqualityComparisonFunction(final Function function) {
        this.function = Objects.requireNonNull(function);
    }

    @Override
    public Value execute(final Value left, final Value right, final NumericalContext numericalContext) {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(numericalContext);
        if (left.isError()) {
            return left;
        } else if (right.isError()) {
            return right;
        } else {
            return function.execute(left, right, numericalContext);
        }
    }

    public static EqualityComparisonFunction ofEqual() {
        return new EqualityComparisonFunction(Function.EQ);
    }

    public static EqualityComparisonFunction ofNotEqual() {
        return new EqualityComparisonFunction(Function.NEQ);
    }

    private enum Function {
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
}
