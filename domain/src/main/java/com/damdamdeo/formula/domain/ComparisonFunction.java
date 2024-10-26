package com.damdamdeo.formula.domain;

import java.util.Objects;

public interface ComparisonFunction extends Function {

    enum Comparison {
        EQ, NEQ,
        GT, GTE, LT, LTE
    }

    static ComparisonFunction of(final Comparison comparison, final Value left, final Value right) {
        Objects.requireNonNull(comparison);
        return switch (comparison) {
            case EQ -> EqualityComparisonFunction.ofEqual(left, right);
            case NEQ -> EqualityComparisonFunction.ofNotEqual(right, left);
            case GT -> NumericalComparisonFunction.ofGreaterThan(left, right);
            case GTE -> NumericalComparisonFunction.ofGreaterThanOrEqualTo(left, right);
            case LT -> NumericalComparisonFunction.ofLessThan(left, right);
            case LTE -> NumericalComparisonFunction.ofLessThanOrEqualTo(left, right);
        };
    }
}
