package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class NumericalComparisonFunctionTestProvider {
    public static Stream<Arguments> provideGreaterThan() {
        return Stream.concat(
                Stream.of(
                        Arguments.of(new Value("660"), new Value("260"), new Value("true")),
                        Arguments.of(new Value("260"), new Value("660"), new Value("false")),
                        Arguments.of(new Value("260"), new Value("260"), new Value("false"))),
                provideCommonResponses());
    }

    public static Stream<Arguments> provideGreaterThanOrEqualTo() {
        return Stream.concat(
                Stream.of(
                        Arguments.of(new Value("660"), new Value("260"), new Value("true")),
                        Arguments.of(new Value("260"), new Value("660"), new Value("false")),
                        Arguments.of(new Value("260"), new Value("260"), new Value("true"))),
                provideCommonResponses());
    }

    public static Stream<Arguments> provideLessThan() {
        return Stream.concat(
                Stream.of(
                        Arguments.of(new Value("660"), new Value("260"), new Value("false")),
                        Arguments.of(new Value("260"), new Value("660"), new Value("true")),
                        Arguments.of(new Value("260"), new Value("260"), new Value("false"))),
                provideCommonResponses());
    }

    public static Stream<Arguments> provideLessThanOrEqualTo() {
        return Stream.concat(
                Stream.of(
                        Arguments.of(new Value("660"), new Value("260"), new Value("false")),
                        Arguments.of(new Value("260"), new Value("660"), new Value("true")),
                        Arguments.of(new Value("260"), new Value("260"), new Value("true"))),
                provideCommonResponses());
    }

    private static Stream<Arguments> provideCommonResponses() {
        return Stream.concat(
                Stream.of(
                        Arguments.of(new Value("#NA!"), new Value("260"), new Value("#NA!")),
                        Arguments.of(new Value("#REF!"), new Value("260"), new Value("#REF!")),
                        Arguments.of(new Value("#NUM!"), new Value("260"), new Value("#NUM!")),
                        Arguments.of(new Value("#DIV/0!"), new Value("260"), new Value("#DIV/0!")),
                        Arguments.of(new Value("660"), new Value("#NA!"), new Value("#NA!")),
                        Arguments.of(new Value("660"), new Value("#REF!"), new Value("#REF!")),
                        Arguments.of(new Value("660"), new Value("#NUM!"), new Value("#NUM!")),
                        Arguments.of(new Value("660"), new Value("#DIV/0!"), new Value("#DIV/0!")),
                        Arguments.of(new Value("azerty"), new Value("260"), new Value("#NUM!")),
                        Arguments.of(new Value("660"), new Value("azerty"), new Value("#NUM!"))
                ),
                FunctionTestProvider.provideCommonResponses()
        );
    }

}
