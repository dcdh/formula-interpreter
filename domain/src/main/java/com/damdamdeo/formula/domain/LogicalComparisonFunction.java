package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.spi.ValueProvider;

import java.util.Objects;

public record LogicalComparisonFunction(Function function, Value comparison, ValueProvider onTrue,
                                        ValueProvider onFalse) implements Function {

    public LogicalComparisonFunction {
        Objects.requireNonNull(function);
        Objects.requireNonNull(comparison);
        Objects.requireNonNull(onTrue);
        Objects.requireNonNull(onFalse);
    }

    @Override
    public Value evaluate(final NumericalContext numericalContext) {
        Objects.requireNonNull(numericalContext);
        return this.function.evaluate(comparison, onTrue, onFalse);
    }

    public static LogicalComparisonFunction ofIf(final Value comparison,
                                                 final ValueProvider onTrue,
                                                 final ValueProvider onFalse) {
        return new LogicalComparisonFunction(Function.IF, comparison, onTrue, onFalse);
    }

    public static LogicalComparisonFunction ofIfError(final Value comparison,
                                                      final ValueProvider onTrue,
                                                      final ValueProvider onFalse) {
        return new LogicalComparisonFunction(Function.IF_ERROR, comparison, onTrue, onFalse);
    }

    public static LogicalComparisonFunction ofIfNotAvailable(final Value comparison,
                                                             final ValueProvider onTrue,
                                                             final ValueProvider onFalse) {
        return new LogicalComparisonFunction(Function.IF_NOT_AVAILABLE, comparison, onTrue, onFalse);
    }

    public static LogicalComparisonFunction of(final Function function,
                                               final Value comparison,
                                               final ValueProvider onTrue,
                                               final ValueProvider onFalse) {
        Objects.requireNonNull(function);
        return switch (function) {
            case IF -> ofIf(comparison, onTrue, onFalse);
            case IF_ERROR -> ofIfError(comparison, onTrue, onFalse);
            case IF_NOT_AVAILABLE -> ofIfNotAvailable(comparison, onTrue, onFalse);
        };
    }

    public enum Function {
        IF {
            @Override
            Value evaluate(final Value comparison,
                           final ValueProvider onTrue,
                           final ValueProvider onFalse) {
                if (comparison.isError()) {
                    return comparison;
                } else if (!comparison.isLogical()) {
                    return Value.ofLogicalValueExpected();
                } else if (comparison.isTrue()) {
                    return onTrue.provide();
                } else if (comparison.isFalse()) {
                    return onFalse.provide();
                } else {
                    throw new IllegalStateException("Should not be here");
                }
            }
        },
        IF_ERROR {
            @Override
            Value evaluate(final Value comparison,
                           final ValueProvider onTrue,
                           final ValueProvider onFalse) {
                if (comparison.isError()) {
                    return onTrue.provide();
                } else {
                    return onFalse.provide();
                }
            }
        },
        IF_NOT_AVAILABLE {
            @Override
            Value evaluate(final Value comparison,
                           final ValueProvider onTrue,
                           final ValueProvider onFalse) {
                if (comparison.isNotAvailable()) {
                    return onTrue.provide();
                } else {
                    return onFalse.provide();
                }
            }
        };

        abstract Value evaluate(Value comparison, ValueProvider onTrue, ValueProvider onFalse);
    }

}
