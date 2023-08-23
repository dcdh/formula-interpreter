package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.infrastructure.antlr.NumericalContext;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

public record Value(String value) implements Input, Result {
    private static final Value TRUE = new Value("true");
    private static final Value FALSE = new Value("false");
    private static final Value ZERO = new Value("0");
    private static final Value ONE = new Value("1");
    private static final Value NOT_AVAILABLE = new Value("#NA!");
    private static final Value UNKNOWN_REF = new Value("#REF!");
    private static final Value NOT_A_NUMERICAL_VALUE = new Value("#NUM!");
    private static final Value DIV_BY_ZERO = new Value("#DIV/0!");

    public static Value ofNotAvailable() {
        return NOT_AVAILABLE;
    }

    public static Value ofUnknownRef() {
        return UNKNOWN_REF;
    }

    public static Value ofNumericalValueExpected() {
        return NOT_A_NUMERICAL_VALUE;
    }

    public static Value ofDividedByZero() {
        return DIV_BY_ZERO;
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

    public Value(final String value) {
        this.value = Objects.requireNonNull(value)
                .replaceAll("^\"|\"$", "");
    }

    public Value(BigDecimal value) {
        this(value.stripTrailingZeros().toPlainString());
    }

    public boolean isNotAvailable() {
        return NOT_AVAILABLE.equals(this);
    }

    public boolean isUnknownRef() {
        return UNKNOWN_REF.equals(this);
    }

    public boolean isNotANumericalValue() {
        return NOT_A_NUMERICAL_VALUE.equals(this);
    }

    public boolean isDivByZero() {
        return DIV_BY_ZERO.equals(this);
    }

    public boolean isError() {
        return isNotAvailable() || isUnknownRef() || isNotANumericalValue() || isDivByZero();
    }

    public boolean isNumeric() {
        if (value.startsWith("#")) {
            return false;
        }
        return value.matches("\\-?[0-9]+.?[0-9]*(E[0-9]+|E\\+[0-9]+|E\\-[0-9]+)?");
    }

    public boolean isText() {
        return !isNotAvailable()
               && !isUnknownRef()
               && !isNotANumericalValue()
               && !isDivByZero()
               && !isTrue()
               && !isFalse()
               && !isBlank()
               && !isNumeric()
                ;
    }

    public boolean isTrue() {
        return TRUE.equals(this) || ONE.equals(this);
    }

    public boolean isFalse() {
        return FALSE.equals(this) || isZero();
    }

    public boolean isZero() {
        return ZERO.equals(this);
    }

    public boolean isBlank() {
        return "".equals(this.value);
    }

    public boolean isLogical() {
        return isTrue() || isFalse();
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
        if (!isNumeric() || !divisor.isNumeric() || divisor.isZero()) {
            throw new IllegalStateException("Should not be here");
        }
        try {
            return new Value(
                    new BigDecimal(value)
                            .divide(new BigDecimal(divisor.value()), numericalContext.scale(), numericalContext.roundingMode())
                            .setScale(numericalContext.scale(), numericalContext.roundingMode())
            );
        } catch (final ArithmeticException arithmeticException) {
            return NOT_A_NUMERICAL_VALUE;
        }
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
        return isTrue() || valueToCheck.isTrue() ? Value.TRUE : Value.FALSE;
    }

    public Value and(final Value valueToCheck) {
        return isTrue() && valueToCheck.isTrue() ? Value.TRUE : Value.FALSE;
    }

    @Override
    public String toString() {
        return "Value{" +
               "value='" + value + '\'' +
               '}';
    }
}
