package com.damdamdeo.formula.domain;

import java.util.Objects;

public final class ArithmeticFunction {
    private final Function function;

    private ArithmeticFunction(final Function function) {
        this.function = Objects.requireNonNull(function);
    }

    public static ArithmeticFunction ofAddition() {
        return new ArithmeticFunction(Function.ADD);
    }

    public static ArithmeticFunction ofSubtraction() {
        return new ArithmeticFunction(Function.SUB);
    }

    public static ArithmeticFunction ofDivision() {
        return new ArithmeticFunction(Function.DIV);
    }

    public static ArithmeticFunction ofMultiplication() {
        return new ArithmeticFunction(Function.MUL);
    }

    public Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(numericalContext);
        if (left.isError()) {
            return left;
        } else if (right.isError()) {
            return right;
        } else if (!left.isNumeric() || !right.isNumeric()) {
            return Value.ofNumericalValueExpected();
        } else if (right.isNumeric() && right.isZero()) {
            return Value.ofDividedByZero();
        } else {
            return function.evaluate(left, right, numericalContext);
        }
    }

    private enum Function {
        ADD {
            @Override
            public Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.add(right, numericalContext);
            }
        },
        SUB {
            @Override
            public Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.subtract(right, numericalContext);
            }
        },
        DIV {
            @Override
            public Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.divide(right, numericalContext);
            }
        },
        MUL {
            @Override
            public Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.multiply(right, numericalContext);
            }
        };

        public abstract Value evaluate(Value left, Value right, NumericalContext numericalContext);
    }
}
