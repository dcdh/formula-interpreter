package com.damdamdeo.formula.structuredreference;

import com.damdamdeo.formula.NumericalContext;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

public record Value(String value) {
    public Value(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public Value(BigDecimal value) {
        this(value.stripTrailingZeros().toPlainString());
    }

    public boolean isNumeric() {
        if (value.startsWith("#")) {
            return false;
        }
        if (!value.matches("[0-9.]+")) {
            return false;
        }
        try {
            new BigDecimal(value);
            return true;
        } catch (final NumberFormatException numberFormatException) {
            return false;
        }
    }

    public Value add(final Value valueToAdd,
                     final NumericalContext numericalContext) {
        if (!isNumeric() || !valueToAdd.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new Value(
                new BigDecimal(value)
                        .add(new BigDecimal(valueToAdd.value()), new MathContext(numericalContext.precision(), numericalContext.roundingMode()))
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())
        );
    }

    public Value subtract(final Value valueToAdd,
                          final NumericalContext numericalContext) {
        if (!isNumeric() || !valueToAdd.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new Value(
                new BigDecimal(value)
                        .subtract(new BigDecimal(valueToAdd.value()), new MathContext(numericalContext.precision(), numericalContext.roundingMode()))
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())
        );
    }

    public Value divide(final Value valueToAdd,
                        final NumericalContext numericalContext) {
        if (!isNumeric() || !valueToAdd.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new Value(
                new BigDecimal(value)
                        .divide(new BigDecimal(valueToAdd.value()), numericalContext.scale(), numericalContext.roundingMode())
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())
        );
    }

    public Value multiply(final Value valueToAdd,
                          final NumericalContext numericalContext) {
        if (!isNumeric() || !valueToAdd.isNumeric()) {
            throw new IllegalStateException("Should not be here");
        }
        return new Value(
                new BigDecimal(value)
                        .multiply(new BigDecimal(valueToAdd.value()), new MathContext(numericalContext.precision(), numericalContext.roundingMode()))
                        .setScale(numericalContext.scale(), numericalContext.roundingMode())
        );
    }

}
