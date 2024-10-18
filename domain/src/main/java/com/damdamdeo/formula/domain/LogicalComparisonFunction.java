package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.spi.ValueProvider;

import java.util.Objects;

public final class LogicalComparisonFunction {
    private final Function function;
    private final ValueProvider onTrue;
    private final ValueProvider onFalse;

    private LogicalComparisonFunction(final Function function,
                                      final ValueProvider onTrue,
                                      final ValueProvider onFalse) {
        this.function = Objects.requireNonNull(function);
        this.onTrue = Objects.requireNonNull(onTrue);
        this.onFalse = Objects.requireNonNull(onFalse);
    }

    public Value evaluate(final Value comparison) {
        return this.function.evaluate(comparison, onTrue, onFalse);
    }

    public static LogicalComparisonFunction ofIf(final ValueProvider onTrue,
                                                 final ValueProvider onFalse) {
        return new LogicalComparisonFunction(Function.IF, onTrue, onFalse);
    }

    public static LogicalComparisonFunction ofIfError(final ValueProvider onTrue,
                                                      final ValueProvider onFalse) {
        return new LogicalComparisonFunction(Function.IF_ERROR, onTrue, onFalse);
    }

    public static LogicalComparisonFunction ofIfNotAvailable(final ValueProvider onTrue,
                                                             final ValueProvider onFalse) {
        return new LogicalComparisonFunction(Function.IF_NOT_AVAILABLE, onTrue, onFalse);
    }

    private enum Function {
        IF {
            @Override
            public Value evaluate(final Value comparison,
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
            public Value evaluate(final Value comparison,
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
            public Value evaluate(final Value comparison,
                                  final ValueProvider onTrue,
                                  final ValueProvider onFalse) {
                if (comparison.isNotAvailable()) {
                    return onTrue.provide();
                } else {
                    return onFalse.provide();
                }
            }
        };

        public abstract Value evaluate(Value comparison, ValueProvider onTrue, ValueProvider onFalse);
    }

}
