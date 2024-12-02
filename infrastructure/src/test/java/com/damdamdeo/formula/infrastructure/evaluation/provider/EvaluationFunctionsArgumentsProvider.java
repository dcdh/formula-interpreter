package com.damdamdeo.formula.infrastructure.evaluation.provider;

import com.damdamdeo.formula.domain.provider.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public final class EvaluationFunctionsArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final List<Arguments> arithmeticAddFunctions = ArithmeticFunctionProviders.byType(ArithmeticFunctionProviders.Type.ADD)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofAdd(function), function.expected())).toList();
        final List<Arguments> arithmeticSubtractFunctions = ArithmeticFunctionProviders.byType(ArithmeticFunctionProviders.Type.SUBTRACT)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofSubtract(function), function.expected())).toList();
        final List<Arguments> arithmeticMultiplyFunctions = ArithmeticFunctionProviders.byType(ArithmeticFunctionProviders.Type.MULTIPLY)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofMultiply(function), function.expected())).toList();
        final List<Arguments> arithmeticDivideFunctions = ArithmeticFunctionProviders.byType(ArithmeticFunctionProviders.Type.DIVIDE)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofDivide(function), function.expected())).toList();

        final List<Arguments> equalityEqualComparisonFunctions = EqualityComparisonFunctionProviders.byType(EqualityComparisonFunctionProviders.Type.EQUAL)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofEqual(function), function.expected())).toList();
        final List<Arguments> equalityNotEqualComparisonFunctions = EqualityComparisonFunctionProviders.byType(EqualityComparisonFunctionProviders.Type.NOT_EQUAL)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofNotEqual(function), function.expected())).toList();

        final List<Arguments> logicalOrBooleanFunctions = LogicalBooleanFunctionProviders.byType(LogicalBooleanFunctionProviders.Type.OR)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofOr(function), function.expected())).toList();
        final List<Arguments> logicalAndBooleanFunctions = LogicalBooleanFunctionProviders.byType(LogicalBooleanFunctionProviders.Type.AND)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofAnd(function), function.expected())).toList();

        final List<Arguments> logicalIfComparisonFunctions = LogicalComparisonFunctionProviders.byType(LogicalComparisonFunctionProviders.Type.IF)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofIf(function), function.expected())).toList();
        final List<Arguments> logicalIfErrorComparisonFunctions = LogicalComparisonFunctionProviders.byType(LogicalComparisonFunctionProviders.Type.IF_ERROR)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofIfError(function), function.expected())).toList();
        final List<Arguments> logicalIfNotAvailableComparisonFunctions = LogicalComparisonFunctionProviders.byType(LogicalComparisonFunctionProviders.Type.IF_NOT_AVAILABLE)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofIfNotAvailable(function), function.expected())).toList();

        final List<Arguments> logicalGTComparisonFunctions = NumericalComparisonFunctionProviders.byType(NumericalComparisonFunctionProviders.Type.GREATER_THAN)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofGt(function), function.expected())).toList();
        final List<Arguments> logicalGTEComparisonFunctions = NumericalComparisonFunctionProviders.byType(NumericalComparisonFunctionProviders.Type.GREATER_THAN_OR_EQUAL_TO)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofGte(function), function.expected())).toList();
        final List<Arguments> logicalLTComparisonFunctions = NumericalComparisonFunctionProviders.byType(NumericalComparisonFunctionProviders.Type.LESS_THAN)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofLt(function), function.expected())).toList();
        final List<Arguments> logicalLTEComparisonFunctions = NumericalComparisonFunctionProviders.byType(NumericalComparisonFunctionProviders.Type.LESS_THAN_OR_EQUAL_TO)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofLte(function), function.expected())).toList();

        final List<Arguments> logicalIsBlankStateFunctions = StateFunctionProviders.byType(StateFunctionProviders.Type.IS_BLANK)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofIsBlank(function), function.expected())).toList();
        final List<Arguments> logicalIsErrorStateFunctions = StateFunctionProviders.byType(StateFunctionProviders.Type.IS_ERROR)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofIsError(function), function.expected())).toList();
        final List<Arguments> logicalIsLogicalStateFunctions = StateFunctionProviders.byType(StateFunctionProviders.Type.IS_LOGICAL)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofIsLogical(function), function.expected())).toList();
        final List<Arguments> logicalIsNotAvailableStateFunctions = StateFunctionProviders.byType(StateFunctionProviders.Type.IS_NOT_AVAILABLE)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofIsNotAvailable(function), function.expected())).toList();
        final List<Arguments> logicalIsNumericStateFunctions = StateFunctionProviders.byType(StateFunctionProviders.Type.IS_NUMERIC)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofIsNumeric(function), function.expected())).toList();
        final List<Arguments> logicalIsTextStateFunctions = StateFunctionProviders.byType(StateFunctionProviders.Type.IS_TEXT)
                .stream()
                .map(function -> Arguments.of(GivenFormula.ofIsText(function), function.expected())).toList();

        return Stream.of(arithmeticAddFunctions,
                        arithmeticSubtractFunctions,
                        arithmeticMultiplyFunctions,
                        arithmeticDivideFunctions,
                        equalityEqualComparisonFunctions,
                        equalityNotEqualComparisonFunctions,
                        logicalOrBooleanFunctions,
                        logicalAndBooleanFunctions,
                        logicalIfComparisonFunctions,
                        logicalIfErrorComparisonFunctions,
                        logicalIfNotAvailableComparisonFunctions,
                        logicalGTComparisonFunctions,
                        logicalGTEComparisonFunctions,
                        logicalLTComparisonFunctions,
                        logicalLTEComparisonFunctions,
                        logicalIsBlankStateFunctions,
                        logicalIsErrorStateFunctions,
                        logicalIsLogicalStateFunctions,
                        logicalIsNotAvailableStateFunctions,
                        logicalIsNumericStateFunctions,
                        logicalIsTextStateFunctions
                )
                .flatMap(Collection::stream);
    }
}
