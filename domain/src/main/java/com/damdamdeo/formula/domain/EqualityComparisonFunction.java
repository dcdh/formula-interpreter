package com.damdamdeo.formula.domain;

import java.util.Objects;

public record EqualityComparisonFunction(Function function, Value left, Value right) implements ComparisonFunction {

    public EqualityComparisonFunction {
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
        } else {
            return function.evaluate(left, right, numericalContext);
        }
    }

    public static EqualityComparisonFunction ofEqual(final Value left, final Value right) {
        return new EqualityComparisonFunction(Function.EQ, left, right);
    }

    public static EqualityComparisonFunction ofNotEqual(final Value left, final Value right) {
        return new EqualityComparisonFunction(Function.NEQ, left, right);
    }

    public enum Function {
        EQ {
            @Override
            Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.equalTo(right, numericalContext);
            }
        },

        NEQ {
            @Override
            Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.notEqualTo(right, numericalContext);
            }
        };

        abstract Value evaluate(Value left, Value right, NumericalContext numericalContext);
    }
}
