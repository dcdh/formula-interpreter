package com.damdamdeo.formula.domain;

import java.util.Objects;

public record NumericalComparisonFunction(Function function, Value left, Value right) implements ComparisonFunction {

    public NumericalComparisonFunction {
        Objects.requireNonNull(function);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
    }

    @Override
    public Value evaluate(final NumericalContext numericalContext) {
        Objects.requireNonNull(numericalContext);
        if (left.isError()) {
            return left;
        } else if (right.isError()) {
            return right;
        } else if (!left.isNumeric() || !right.isNumeric()) {
            return Value.ofNumericalValueExpected();
        } else {
            return function.evaluate(left, right, numericalContext);
        }
    }

    public static NumericalComparisonFunction ofGreaterThan(final Value left, final Value right) {
        return new NumericalComparisonFunction(Function.GT, left, right);
    }

    public static NumericalComparisonFunction ofGreaterThanOrEqualTo(final Value left, final Value right) {
        return new NumericalComparisonFunction(Function.GTE, left, right);
    }

    public static NumericalComparisonFunction ofLessThan(final Value left, final Value right) {
        return new NumericalComparisonFunction(Function.LT, left, right);
    }

    public static NumericalComparisonFunction ofLessThanOrEqualTo(final Value left, final Value right) {
        return new NumericalComparisonFunction(Function.LTE, left, right);
    }

    public enum Function {
        GT {
            @Override
            Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.greaterThan(right, numericalContext);
            }
        },
        GTE {
            @Override
            Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.greaterThanOrEqualTo(right, numericalContext);
            }
        },
        LT {
            @Override
            Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.lessThan(right, numericalContext);
            }
        },
        LTE {
            @Override
            Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.lessThanOrEqualTo(right, numericalContext);
            }
        };

        abstract Value evaluate(Value left, Value right, NumericalContext numericalContext);
    }
}
