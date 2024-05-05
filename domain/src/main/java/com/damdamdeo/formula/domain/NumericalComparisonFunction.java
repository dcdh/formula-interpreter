package com.damdamdeo.formula.domain;

import java.util.Objects;

public final class NumericalComparisonFunction implements ComparisonFunction {
    private final Function function;

    private NumericalComparisonFunction(final Function function) {
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
        } else if (!left.isNumeric() || !right.isNumeric()) {
            return Value.ofNumericalValueExpected();
        } else {
            return function.execute(left, right, numericalContext);
        }
    }

    public static NumericalComparisonFunction ofGreaterThan() {
        return new NumericalComparisonFunction(Function.GT);
    }

    public static NumericalComparisonFunction ofGreaterThanOrEqualTo() {
        return new NumericalComparisonFunction(Function.GTE);
    }

    public static NumericalComparisonFunction ofLessThan() {
        return new NumericalComparisonFunction(Function.LT);
    }

    public static NumericalComparisonFunction ofLessThanOrEqualTo() {
        return new NumericalComparisonFunction(Function.LTE);
    }

    private enum Function {
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
}
