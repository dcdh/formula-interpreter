package com.damdamdeo.formula.domain;

import java.util.Objects;

public record ArithmeticFunction(Function function, Value left, Value right) implements Function {

    public ArithmeticFunction {
        Objects.requireNonNull(function);
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
    }

    public static ArithmeticFunction ofAddition(final Value left, final Value right) {
        return new ArithmeticFunction(Function.ADD, left, right);
    }

    public static ArithmeticFunction ofSubtraction(final Value left, final Value right) {
        return new ArithmeticFunction(Function.SUB, left, right);
    }

    public static ArithmeticFunction ofDivision(final Value left, final Value right) {
        return new ArithmeticFunction(Function.DIV, left, right);
    }

    public static ArithmeticFunction ofMultiplication(final Value left, final Value right) {
        return new ArithmeticFunction(Function.MUL, left, right);
    }

    public static ArithmeticFunction of(final Function function, final Value left, final Value right) {
        Objects.requireNonNull(function);
        return switch (function) {
            case ADD -> ofAddition(left, right);
            case SUB -> ofSubtraction(left, right);
            case DIV -> ofDivision(left, right);
            case MUL -> ofMultiplication(left, right);
        };
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
        } else if (right.isNumeric() && right.isZero()) {
            return Value.ofDividedByZero();
        } else {
            return function.evaluate(left, right, numericalContext);
        }
    }

    public enum Function {
        ADD {
            @Override
            Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.add(right, numericalContext);
            }
        },
        SUB {
            @Override
            Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.subtract(right, numericalContext);
            }
        },
        DIV {
            @Override
            Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.divide(right, numericalContext);
            }
        },
        MUL {
            @Override
            Value evaluate(final Value left, final Value right, final NumericalContext numericalContext) {
                return left.multiply(right, numericalContext);
            }
        };

        abstract Value evaluate(Value left, Value right, NumericalContext numericalContext);
    }
}
