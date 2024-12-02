package com.damdamdeo.formula.infrastructure.evaluation.provider;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.provider.*;

import java.util.Objects;

public final class GivenFormula {
    private final Formula formula;

    private GivenFormula(final Formula formula) {
        this.formula = Objects.requireNonNull(formula);
    }

    public static GivenFormula ofAdd(final ArithmeticFunctionProviders.ArithmeticFunction arithmeticFunction) {
        return new GivenFormula(
                new Formula("ADD(%s,%s)".formatted(arithmeticFunction.givenLeft().value().value(),
                        arithmeticFunction.givenRight().value().value()))
        );
    }

    public static GivenFormula ofSubtract(final ArithmeticFunctionProviders.ArithmeticFunction arithmeticFunction) {
        return new GivenFormula(
                new Formula("SUB(%s,%s)".formatted(arithmeticFunction.givenLeft().value().value(),
                        arithmeticFunction.givenRight().value().value()))
        );
    }

    public static GivenFormula ofMultiply(final ArithmeticFunctionProviders.ArithmeticFunction arithmeticFunction) {
        return new GivenFormula(
                new Formula("MUL(%s,%s)".formatted(arithmeticFunction.givenLeft().value().value(),
                        arithmeticFunction.givenRight().value().value()))
        );
    }

    public static GivenFormula ofDivide(final ArithmeticFunctionProviders.ArithmeticFunction arithmeticFunction) {
        return new GivenFormula(
                new Formula("DIV(%s,%s)".formatted(arithmeticFunction.givenLeft().value().value(),
                        arithmeticFunction.givenRight().value().value()))
        );
    }

    public static GivenFormula ofEqual(final EqualityComparisonFunctionProviders.EqualityComparisonFunction equalityComparisonFunction) {
        return new GivenFormula(
                new Formula("EQ(%s,%s)".formatted(equalityComparisonFunction.givenLeft().value().value(),
                        equalityComparisonFunction.givenRight().value().value()))
        );
    }

    public static GivenFormula ofNotEqual(final EqualityComparisonFunctionProviders.EqualityComparisonFunction equalityComparisonFunction) {
        return new GivenFormula(
                new Formula("NEQ(%s,%s)".formatted(equalityComparisonFunction.givenLeft().value().value(),
                        equalityComparisonFunction.givenRight().value().value()))
        );
    }

    public static GivenFormula ofOr(final LogicalBooleanFunctionProviders.LogicalBooleanFunction logicalBooleanFunction) {
        return new GivenFormula(
                new Formula("OR(%s,%s)".formatted(logicalBooleanFunction.givenLeft().value().value(),
                        logicalBooleanFunction.givenRight().value().value()))
        );
    }

    public static GivenFormula ofAnd(final LogicalBooleanFunctionProviders.LogicalBooleanFunction logicalBooleanFunction) {
        return new GivenFormula(
                new Formula("AND(%s,%s)".formatted(logicalBooleanFunction.givenLeft().value().value(),
                        logicalBooleanFunction.givenRight().value().value()))
        );
    }

    public static GivenFormula ofIf(final LogicalComparisonFunctionProviders.LogicalComparisonFunction logicalComparisonFunction) {
        return new GivenFormula(
                new Formula("IF(%s,true,false)".formatted(logicalComparisonFunction.givenComparison().value().value()))
        );
    }

    public static GivenFormula ofIfError(final LogicalComparisonFunctionProviders.LogicalComparisonFunction logicalComparisonFunction) {
        return new GivenFormula(
                new Formula("IFERROR(%s,true,false)".formatted(logicalComparisonFunction.givenComparison().value().value()))
        );
    }

    public static GivenFormula ofIfNotAvailable(final LogicalComparisonFunctionProviders.LogicalComparisonFunction logicalComparisonFunction) {
        return new GivenFormula(
                new Formula("IFNA(%s,true,false)".formatted(logicalComparisonFunction.givenComparison().value().value()))
        );
    }

    public static GivenFormula ofGt(final NumericalComparisonFunctionProviders.NumericalComparisonFunction numericalComparisonFunction) {
        return new GivenFormula(
                new Formula("GT(%s,%s)".formatted(numericalComparisonFunction.givenLeft().value().value(),
                        numericalComparisonFunction.givenRight().value().value()))
        );
    }

    public static GivenFormula ofGte(final NumericalComparisonFunctionProviders.NumericalComparisonFunction numericalComparisonFunction) {
        return new GivenFormula(
                new Formula("GTE(%s,%s)".formatted(numericalComparisonFunction.givenLeft().value().value(),
                        numericalComparisonFunction.givenRight().value().value()))
        );
    }

    public static GivenFormula ofLt(final NumericalComparisonFunctionProviders.NumericalComparisonFunction numericalComparisonFunction) {
        return new GivenFormula(
                new Formula("LT(%s,%s)".formatted(numericalComparisonFunction.givenLeft().value().value(),
                        numericalComparisonFunction.givenRight().value().value()))
        );
    }

    public static GivenFormula ofLte(final NumericalComparisonFunctionProviders.NumericalComparisonFunction numericalComparisonFunction) {
        return new GivenFormula(
                new Formula("LTE(%s,%s)".formatted(numericalComparisonFunction.givenLeft().value().value(),
                        numericalComparisonFunction.givenRight().value().value()))
        );
    }

    public static GivenFormula ofIsBlank(final StateFunctionProviders.StateFunction stateFunction) {
        return new GivenFormula(
                new Formula("ISBLANK(%s)".formatted(stateFunction.givenValue().value().value()))
        );
    }

    public static GivenFormula ofIsError(final StateFunctionProviders.StateFunction stateFunction) {
        return new GivenFormula(
                new Formula("ISERROR(%s)".formatted(stateFunction.givenValue().value().value()))
        );
    }

    public static GivenFormula ofIsLogical(final StateFunctionProviders.StateFunction stateFunction) {
        return new GivenFormula(
                new Formula("ISLOGICAL(%s)".formatted(stateFunction.givenValue().value().value()))
        );
    }

    public static GivenFormula ofIsNotAvailable(final StateFunctionProviders.StateFunction stateFunction) {
        return new GivenFormula(
                new Formula("ISNA(%s)".formatted(stateFunction.givenValue().value().value()))
        );
    }

    public static GivenFormula ofIsNumeric(final StateFunctionProviders.StateFunction stateFunction) {
        return new GivenFormula(
                new Formula("ISNUM(%s)".formatted(stateFunction.givenValue().value().value()))
        );
    }

    public static GivenFormula ofIsText(final StateFunctionProviders.StateFunction stateFunction) {
        return new GivenFormula(
                new Formula("ISTEXT(%s)".formatted(stateFunction.givenValue().value().value()))
        );
    }

    public Formula formula() {
        return formula;
    }

    @Override
    public String toString() {
        return formula.toString();
    }
}