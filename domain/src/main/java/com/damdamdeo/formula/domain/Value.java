package com.damdamdeo.formula.domain;

import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Value implements InputValue {
    private static final Pattern IS_NUMERIC_PATTERN = Pattern.compile("^\\-?[0-9]+.?[0-9]*(E[0-9]+|E\\+[0-9]+|E\\-[0-9]+)?$");

    private static final Value TRUE = new Value("true");
    private static final Value FALSE = new Value("false");
    private static final Value ZERO = new Value("0");
    private static final Value ONE = new Value("1");
    private static final Value EMPTY = new Value("");
    private static final Value NOT_AVAILABLE = new Value("#NA!");
    private static final Value UNKNOWN_REF = new Value("#REF!");
    private static final Value NOT_A_NUMERICAL_VALUE = new Value("#NUM!");
    private static final Value DIV_BY_ZERO = new Value("#DIV/0!");
    private static final Value NOT_A_LOGICAL_VALUE = new Value("#LOG!");

    private final String value;

    public static Value ofNotAvailable() {
        return NOT_AVAILABLE;
    }

    public static Value ofUnknownRef() {
        return UNKNOWN_REF;
    }

    public static Value ofNotANumericalValue() {
        return NOT_A_NUMERICAL_VALUE;
    }

    public static Value ofDividedByZero() {
        return DIV_BY_ZERO;
    }

    public static Value ofNotALogicalValue() {
        return NOT_A_LOGICAL_VALUE;
    }

    public static Value ofTrue() {
        return Value.TRUE;
    }

    public static Value ofZero() {
        return Value.ZERO;
    }

    public static Value ofFalse() {
        return Value.FALSE;
    }

    public static Value ofOne() {
        return Value.ONE;
    }

    public static Value ofEmpty() {
        return Value.EMPTY;
    }

    public static Value ofText(final String text) {
        final Value value = new Value(text);
        Validate.validState(value.isText());
        return value;
    }

    public static Value ofNumeric(final String numeric) {
        final Value value = new Value(numeric);
        Validate.validState(value.isNumeric());
        return value;
    }

    public static Value ofBoolean(final String booleanValue) {
        final Value value = new Value(booleanValue);
        Validate.validState(value.isBoolean());
        return value;
    }

    public static Value ofAny(final String any) {
        return new Value(any);
    }

    private Value(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    private Value(final BigDecimal value) {
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

    public boolean isNotALogicalValue() {
        return NOT_A_LOGICAL_VALUE.equals(this);
    }

    public boolean isError() {
        return isNotAvailable() || isUnknownRef() || isNotANumericalValue() || isDivByZero() || isNotALogicalValue();
    }

    public boolean isNumeric() {
        if (value.startsWith("#")) {
            return false;
        }
        if (value.startsWith("\"")) {
            return false;
        }
        return IS_NUMERIC_PATTERN.matcher(value).matches();
    }

    public boolean isText() {
        return value.startsWith("\"") && value.endsWith("\"");
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

    public boolean isBoolean() {
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

    public Value greaterThan(final Value valueToCheckAgainst,
                             final NumericalContext numericalContext) {
        if (!isNumeric() || !valueToCheckAgainst.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new BigDecimal(value)
                .setScale(numericalContext.scale(), numericalContext.roundingMode())
                .compareTo(new BigDecimal(valueToCheckAgainst.value())
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())) > 0 ? Value.TRUE : Value.FALSE;
    }

    public Value greaterThanOrEqualTo(final Value valueToCheckAgainst,
                                      final NumericalContext numericalContext) {
        if (!isNumeric() || !valueToCheckAgainst.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new BigDecimal(value)
                .setScale(numericalContext.scale(), numericalContext.roundingMode())
                .compareTo(new BigDecimal(valueToCheckAgainst.value())
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())) >= 0 ? Value.TRUE : Value.FALSE;
    }

    public Value equalTo(final Value valueToCheckAgainst,
                         final NumericalContext numericalContext) {
        if (!isNumeric() && valueToCheckAgainst.isNumeric()) {
            return Value.FALSE;
        }
        if (isNumeric() && !valueToCheckAgainst.isNumeric()) {
            return Value.FALSE;
        }
        if (isNumeric() && isNumeric()) {
            return new BigDecimal(value)
                    .setScale(numericalContext.scale(), numericalContext.roundingMode())
                    .compareTo(new BigDecimal(valueToCheckAgainst.value())
                            .setScale(numericalContext.scale(), numericalContext.roundingMode())) == 0 ? Value.TRUE : Value.FALSE;
        }
        return value.equals(valueToCheckAgainst.value()) ? Value.TRUE : Value.FALSE;
    }

    public Value notEqualTo(final Value valueToCheckAgainst,
                            final NumericalContext numericalContext) {
        if (!isNumeric() && valueToCheckAgainst.isNumeric()) {
            return Value.TRUE;
        }
        if (isNumeric() && !valueToCheckAgainst.isNumeric()) {
            return Value.TRUE;
        }
        if (isNumeric() && isNumeric()) {
            return new BigDecimal(value)
                    .setScale(numericalContext.scale(), numericalContext.roundingMode())
                    .compareTo(new BigDecimal(valueToCheckAgainst.value())
                            .setScale(numericalContext.scale(), numericalContext.roundingMode())) != 0 ? Value.TRUE : Value.FALSE;
        }
        return value.equals(valueToCheckAgainst.value()) ? Value.FALSE : Value.TRUE;
    }

    public Value lessThan(final Value valueToCheckAgainst,
                          final NumericalContext numericalContext) {
        if (!isNumeric() || !valueToCheckAgainst.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new BigDecimal(value)
                .setScale(numericalContext.scale(), numericalContext.roundingMode())
                .compareTo(new BigDecimal(valueToCheckAgainst.value())
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())) < 0 ? Value.TRUE : Value.FALSE;
    }

    public Value lessThanOrEqualTo(final Value valueToCheckAgainst,
                                   final NumericalContext numericalContext) {
        if (!isNumeric() || !valueToCheckAgainst.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new BigDecimal(value)
                .setScale(numericalContext.scale(), numericalContext.roundingMode())
                .compareTo(new BigDecimal(valueToCheckAgainst.value())
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())) <= 0 ? Value.TRUE : Value.FALSE;
    }

    public Value or(final Value valueToCheckAgainst) {
        return isTrue() || valueToCheckAgainst.isTrue() ? Value.TRUE : Value.FALSE;
    }

    public Value and(final Value valueToCheckAgainst) {
        return isTrue() && valueToCheckAgainst.isTrue() ? Value.TRUE : Value.FALSE;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Value value1 = (Value) o;
        return Objects.equals(value, value1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Value{" +
                "value='" + value + '\'' +
                '}';
    }
}
