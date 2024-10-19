package com.damdamdeo.formula.domain;

import java.util.Objects;

public interface ComparisonFunction extends Function {

    enum ComparisonType {
        EQ, NEQ,
        GT, GTE, LT, LTE
    }

    static ComparisonFunction of(final ComparisonType comparisonType, final Value left, final Value right) {
        Objects.requireNonNull(comparisonType);
        return switch (comparisonType) {
            case EQ -> EqualityComparisonFunction.ofEqual(left, right);
            case NEQ -> EqualityComparisonFunction.ofNotEqual(right, left);
            case GT -> NumericalComparisonFunction.ofGreaterThan(left, right);
            case GTE -> NumericalComparisonFunction.ofGreaterThanOrEqualTo(left, right);
            case LT -> NumericalComparisonFunction.ofLessThan(left, right);
            case LTE -> NumericalComparisonFunction.ofLessThanOrEqualTo(left, right);
        };
    }
}
