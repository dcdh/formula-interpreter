package com.damdamdeo.formula;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

public record Value(String value) implements Result {

    private static Value TRUE = new Value("true");
    private static Value FALSE = new Value("false");
    private static Value ONE = new Value("1");
    private static Value UNKNOWN = new Value("#REF!");
    private static Value ERROR = new Value("#VALUE!");

    public static Value of() {
        return new Value("");
    }

    public static Value ofUnknown() {
        return UNKNOWN;
    }

    public static Value ofError() {
        return ERROR;
    }

    public static Value ofTrue() {
        return Value.TRUE;
    }

    public static Value ofFalse() {
        return Value.FALSE;
    }

    public static Value of(final String value) {
        return new Value(value);
    }

    public Value(String value) {
        this.value = Objects.requireNonNull(value)
                .replaceAll("^\"|\"$", "");
    }

    public Value(BigDecimal value) {
        this(value.stripTrailingZeros().toPlainString());
    }

    public boolean isUnknown() {
        return UNKNOWN.equals(this);
    }

    public boolean isError() {
        return ERROR.equals(this);
    }

    public boolean isNumeric() {
        if (value.startsWith("#")) {
            return false;
        }
        return value.matches("\\-?[0-9]+.?[0-9]*(E[0-9]+|E\\+[0-9]+|E\\-[0-9]+)?");
    }

    public boolean isTrue() {
        return TRUE.equals(this);
    }

    public boolean isOne() {
        return ONE.equals(this);
    }

    public Value add(final Value augend,
                     final NumericalContext numericalContext) {
        if (!isNumeric() || !augend.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new Value(
                new BigDecimal(value)
                        .add(new BigDecimal(augend.value()), new MathContext(numericalContext.precision(), numericalContext.roundingMode()))
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())
        );
    }

    public Value subtract(final Value subtrahend,
                          final NumericalContext numericalContext) {
        if (!isNumeric() || !subtrahend.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new Value(
                new BigDecimal(value)
                        .subtract(new BigDecimal(subtrahend.value()), new MathContext(numericalContext.precision(), numericalContext.roundingMode()))
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())
        );
    }

    public Value divide(final Value divisor,
                        final NumericalContext numericalContext) {
        if (!isNumeric() || !divisor.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new Value(
                new BigDecimal(value)
                        .divide(new BigDecimal(divisor.value()), numericalContext.scale(), numericalContext.roundingMode())
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())
        );
    }

    public Value multiply(final Value multiplicand,
                          final NumericalContext numericalContext) {
        if (!isNumeric() || !multiplicand.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new Value(
                new BigDecimal(value)
                        .multiply(new BigDecimal(multiplicand.value()), new MathContext(numericalContext.precision(), numericalContext.roundingMode()))
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())
        );
    }

    public Value greaterThan(final Value valueToCheck,
                             final NumericalContext numericalContext) {
        if (!isNumeric() || !valueToCheck.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new BigDecimal(value)
                .setScale(numericalContext.scale(), numericalContext.roundingMode())
                .compareTo(new BigDecimal(valueToCheck.value())
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())) > 0 ? Value.TRUE : Value.FALSE;
    }

    public Value greaterThanOrEqualTo(final Value valueToCheck,
                                      final NumericalContext numericalContext) {
        if (!isNumeric() || !valueToCheck.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new BigDecimal(value)
                .setScale(numericalContext.scale(), numericalContext.roundingMode())
                .compareTo(new BigDecimal(valueToCheck.value())
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())) >= 0 ? Value.TRUE : Value.FALSE;
    }

    public Value equalTo(final Value valueToCheck,
                         final NumericalContext numericalContext) {
        if (!isNumeric() && valueToCheck.isNumeric()) {
            return Value.FALSE;
        }
        if (isNumeric() && !valueToCheck.isNumeric()) {
            return Value.FALSE;
        }
        if (isNumeric() && isNumeric()) {
            return new BigDecimal(value)
                    .setScale(numericalContext.scale(), numericalContext.roundingMode())
                    .compareTo(new BigDecimal(valueToCheck.value())
                            .setScale(numericalContext.scale(), numericalContext.roundingMode())) == 0 ? Value.TRUE : Value.FALSE;
        }
        return value.equals(valueToCheck.value()) ? Value.TRUE : Value.FALSE;
    }

    public Value notEqualTo(final Value valueToCheck,
                            final NumericalContext numericalContext) {
        if (!isNumeric() && valueToCheck.isNumeric()) {
            return Value.TRUE;
        }
        if (isNumeric() && !valueToCheck.isNumeric()) {
            return Value.TRUE;
        }
        if (isNumeric() && isNumeric()) {
            return new BigDecimal(value)
                    .setScale(numericalContext.scale(), numericalContext.roundingMode())
                    .compareTo(new BigDecimal(valueToCheck.value())
                            .setScale(numericalContext.scale(), numericalContext.roundingMode())) != 0 ? Value.TRUE : Value.FALSE;
        }
        return value.equals(valueToCheck.value()) ? Value.FALSE : Value.TRUE;
    }

    public Value lessThan(final Value valueToCheck,
                          final NumericalContext numericalContext) {
        if (!isNumeric() || !valueToCheck.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new BigDecimal(value)
                .setScale(numericalContext.scale(), numericalContext.roundingMode())
                .compareTo(new BigDecimal(valueToCheck.value())
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())) < 0 ? Value.TRUE : Value.FALSE;
    }

    public Value lessThanOrEqualTo(final Value valueToCheck,
                                   final NumericalContext numericalContext) {
        if (!isNumeric() || !valueToCheck.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new BigDecimal(value)
                .setScale(numericalContext.scale(), numericalContext.roundingMode())
                .compareTo(new BigDecimal(valueToCheck.value())
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())) <= 0 ? Value.TRUE : Value.FALSE;
    }

    public Value or(final Value valueToCheck) {
        return (isTrue() || isOne()) || (valueToCheck.isTrue() || valueToCheck.isOne()) ? Value.TRUE : Value.FALSE;
    }

    public Value and(final Value valueToCheck) {
        return (isTrue() || isOne()) && (valueToCheck.isTrue() || valueToCheck.isOne()) ? Value.TRUE : Value.FALSE;
    }

}