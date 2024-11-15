package com.damdamdeo.formula.domain.evaluation.provider;

import com.damdamdeo.formula.domain.ComparisonFunction;
import com.damdamdeo.formula.domain.provider.EqualityComparisonFunctionTestProvider;
import com.damdamdeo.formula.domain.provider.NumericalComparisonFunctionTestProvider;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ComparisonExpressionTestProvider {

    public static Map<ComparisonFunction.Comparison, Supplier<Stream<Arguments>>> ARGUMENTS_BY_COMPARISON_FUNC = Map.of(
            ComparisonFunction.Comparison.EQ, EqualityComparisonFunctionTestProvider::provideEqual,
            ComparisonFunction.Comparison.NEQ, EqualityComparisonFunctionTestProvider::provideNotEqual,
            ComparisonFunction.Comparison.GT, NumericalComparisonFunctionTestProvider::provideGreaterThan,
            ComparisonFunction.Comparison.GTE, NumericalComparisonFunctionTestProvider::provideGreaterThanOrEqualTo,
            ComparisonFunction.Comparison.LT, NumericalComparisonFunctionTestProvider::provideLessThan,
            ComparisonFunction.Comparison.LTE, NumericalComparisonFunctionTestProvider::provideLessThanOrEqualTo
    );

    dans les faits je devrais ne faire qu'un seul test ...

//    FCK putain la position va changer ... du coup je devrais faire une map pour
//
//    FCK last one !
//
//    je dois faire une methode par type de comparaison ou bien un seul avec une map ...
//    ou deux avec une map chacun ...
//     // TODO FCK Faire de meme avec EQ, NEQ, GT, GTE ... putain je vais m'amuser ...
    // FCK TODO passer par une putain de MAP avec les fonctions qui vont biens !!!!

//    la cela va être compliqué parce que GT et GTE ne partage pas la même longieur etEqualityComparisonFunctionTestProvider di coup la meme position
//    ok je dois reprendre avec EqualityComparisonFunctionTestProvider
//
//    private static Stream<Arguments> provideComparisonsWithExpectedValues() {
//        return Stream.of(
//                        NumericalComparisonFunctionTestProvider.provideGreaterThan()
//                                .map(greaterThan -> Arguments.of(greaterThan.get()[0], "GT", greaterThan.get()[1], greaterThan.get()[2])),
//                        NumericalComparisonFunctionTestProvider.provideGreaterThanOrEqualTo()
//                                .map(greaterThanOrEqualTo -> Arguments.of(greaterThanOrEqualTo.get()[0], "GTE", greaterThanOrEqualTo.get()[1], greaterThanOrEqualTo.get()[2])),
//                        NumericalComparisonFunctionTestProvider.provideLessThan()
//                                .map(lessThan -> Arguments.of(lessThan.get()[0], "LT", lessThan.get()[1], lessThan.get()[2])),
//                        NumericalComparisonFunctionTestProvider.provideLessThanOrEqualTo()
//                                .map(lessThanOrEqualTo -> Arguments.of(lessThanOrEqualTo.get()[0], "LTE", lessThanOrEqualTo.get()[1], lessThanOrEqualTo.get()[2])),
//                        NumericalComparisonFunctionTestProvider.provideCommonResponses()
//                                .flatMap(numericalCommonResponse -> Stream.of("GT", "GTE", "LT", "LTE")
//                                        .map(function -> Arguments.of(numericalCommonResponse.get()[0], function, numericalCommonResponse.get()[1], numericalCommonResponse.get()[2]))),
//                        EqualityComparisonFunctionTestProvider.provideEqual()
//                                .map(equal -> Arguments.of(equal.get()[0], "EQ", equal.get()[1], equal.get()[2])),
//                        EqualityComparisonFunctionTestProvider.provideNotEqual()
//                                .map(notEqual -> Arguments.of(notEqual.get()[0], "NEQ", notEqual.get()[1], notEqual.get()[2])),
//                        EqualityComparisonFunctionTestProvider.provideCommonResponses()
//                                .flatMap(comparisonCommonResponse -> Stream.of("EQ", "NEQ")
//                                        .map(function -> Arguments.of(comparisonCommonResponse.get()[0], function, comparisonCommonResponse.get()[1], comparisonCommonResponse.get()[2])))
//                )
//                .flatMap(Function.identity());
//    }



}
