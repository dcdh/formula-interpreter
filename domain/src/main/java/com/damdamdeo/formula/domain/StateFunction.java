package com.damdamdeo.formula.domain;

import java.util.Objects;
import java.util.function.Function;

public final class StateFunction {
    private final Function<Value, Boolean> functionToExecute;

    private StateFunction(final Function<Value, Boolean> functionToExecute) {
        this.functionToExecute = Objects.requireNonNull(functionToExecute);
    }

    public boolean evaluate(final Value argument) {
        return this.functionToExecute.apply(argument);
    }

    public static StateFunction ofIsNotAvailable() {
        return new StateFunction(Value::isNotAvailable);
    }

    public static StateFunction ofIsError() {
        return new StateFunction(Value::isError);
    }

    public static StateFunction ofIsNumeric() {
        return new StateFunction(Value::isNumeric);
    }

    public static StateFunction ofIsText() {
        return new StateFunction(Value::isText);
    }

    public static StateFunction ofIsBlank() {
        return new StateFunction(Value::isBlank);
    }

    public static StateFunction ofIsLogical() {
        return new StateFunction(Value::isLogical);
    }
}
