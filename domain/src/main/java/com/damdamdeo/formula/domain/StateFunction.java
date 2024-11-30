package com.damdamdeo.formula.domain;

import java.util.Objects;

public record StateFunction(java.util.function.Function<Value, Boolean> functionToExecute,
                            Value argument) implements Function {

    public StateFunction {
        Objects.requireNonNull(functionToExecute);
        Objects.requireNonNull(argument);
    }

    @Override
    public Value evaluate(final NumericalContext numericalContext) {
        Objects.requireNonNull(numericalContext);
        if (this.functionToExecute.apply(argument)) {
            return Value.ofTrue();
        }
        return Value.ofFalse();
    }

    public static StateFunction ofIsNotAvailable(final Value argument) {
        return new StateFunction(Value::isNotAvailable, argument);
    }

    public static StateFunction ofIsError(final Value argument) {
        return new StateFunction(Value::isError, argument);
    }

    public static StateFunction ofIsNumeric(final Value argument) {
        return new StateFunction(Value::isNumeric, argument);
    }

    public static StateFunction ofIsText(final Value argument) {
        return new StateFunction(Value::isValidText, argument);
    }

    public static StateFunction ofIsBlank(final Value argument) {
        return new StateFunction(Value::isBlank, argument);
    }

    public static StateFunction ofIsLogical(final Value argument) {
        return new StateFunction(Value::isBoolean, argument);
    }

    public static StateFunction of(final Function function, final Value argument) {
        Objects.requireNonNull(function);
        return switch (function) {
            case IS_NOT_AVAILABLE -> ofIsNotAvailable(argument);
            case IS_ERROR -> ofIsError(argument);
            case IS_NUMERIC -> ofIsNumeric(argument);
            case IS_TEXT -> ofIsText(argument);
            case IS_BLANK -> ofIsBlank(argument);
            case IS_LOGICAL -> ofIsLogical(argument);
        };
    }

    public enum Function {
        IS_NOT_AVAILABLE,
        IS_ERROR,
        IS_NUMERIC,
        IS_TEXT,
        IS_BLANK,
        IS_LOGICAL,
    }
}
