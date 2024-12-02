package com.damdamdeo.formula.domain;

import java.util.Objects;

public record StateFunction(java.util.function.Function<Value, Boolean> functionToExecute,
                            Value reference) implements Function {

    public StateFunction {
        Objects.requireNonNull(functionToExecute);
        Objects.requireNonNull(reference);
    }

    @Override
    public Value evaluate(final NumericalContext numericalContext) {
        Objects.requireNonNull(numericalContext);
        if (this.functionToExecute.apply(reference)) {
            return Value.ofTrue();
        }
        return Value.ofFalse();
    }

    public static StateFunction ofIsNotAvailable(final Value reference) {
        return new StateFunction(Value::isNotAvailable, reference);
    }

    public static StateFunction ofIsError(final Value reference) {
        return new StateFunction(Value::isError, reference);
    }

    public static StateFunction ofIsNumeric(final Value reference) {
        return new StateFunction(Value::isNumeric, reference);
    }

    public static StateFunction ofIsText(final Value reference) {
        return new StateFunction(Value::isText, reference);
    }

    public static StateFunction ofIsBlank(final Value reference) {
        return new StateFunction(Value::isBlank, reference);
    }

    public static StateFunction ofIsLogical(final Value reference) {
        return new StateFunction(Value::isBoolean, reference);
    }

    public static StateFunction of(final Function function, final Value reference) {
        Objects.requireNonNull(function);
        return switch (function) {
            case IS_NOT_AVAILABLE -> ofIsNotAvailable(reference);
            case IS_ERROR -> ofIsError(reference);
            case IS_NUMERIC -> ofIsNumeric(reference);
            case IS_TEXT -> ofIsText(reference);
            case IS_BLANK -> ofIsBlank(reference);
            case IS_LOGICAL -> ofIsLogical(reference);
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
