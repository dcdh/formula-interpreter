package com.damdamdeo.formula.domain;

import java.util.Objects;
import java.util.function.Function;

public final class InformationFunction {
    private final Function<Value, Boolean> functionToExecute;

    private InformationFunction(final Function<Value, Boolean> functionToExecute) {
        this.functionToExecute = Objects.requireNonNull(functionToExecute);
    }

    public boolean execute(final Value argument) {
        return this.functionToExecute.apply(argument);
    }

    public static InformationFunction ofIsNotAvailable() {
        return new InformationFunction(Value::isNotAvailable);
    }

    public static InformationFunction ofIsError() {
        return new InformationFunction(Value::isError);
    }

    public static InformationFunction ofIsNumeric() {
        return new InformationFunction(Value::isNumeric);
    }

    public static InformationFunction ofIsText() {
        return new InformationFunction(Value::isText);
    }

    public static InformationFunction ofIsBlank() {
        return new InformationFunction(Value::isBlank);
    }

    public static InformationFunction ofIsLogical() {
        return new InformationFunction(Value::isLogical);
    }
}
