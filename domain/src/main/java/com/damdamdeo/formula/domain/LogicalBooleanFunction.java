package com.damdamdeo.formula.domain;

import java.util.Objects;

public record LogicalBooleanFunction(Function function, Value left, Value right) implements Function {

    public LogicalBooleanFunction {
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
        } else if (!left.isLogical() || !right.isLogical()) {
            return Value.ofNotALogicalValue();
        } else {
            return function.evaluate(left, right);
        }
    }

    public static LogicalBooleanFunction ofAnd(final Value left, final Value right) {
        return new LogicalBooleanFunction(Function.AND, left, right);
    }

    public static LogicalBooleanFunction ofOr(final Value left, final Value right) {
        return new LogicalBooleanFunction(Function.OR, left, right);
    }

    public static LogicalBooleanFunction of(final Function function, final Value left, final Value right) {
        Objects.requireNonNull(function);
        return switch (function) {
            case AND -> ofAnd(left, right);
            case OR -> ofOr(left, right);
        };
    }

    public enum Function {

        OR {
            @Override
            Value evaluate(final Value left, final Value right) {
                return left.or(right);
            }
        },
        AND {
            @Override
            Value evaluate(final Value left, final Value right) {
                return left.and(right);
            }
        };

        abstract Value evaluate(Value left, Value right);
    }
}
