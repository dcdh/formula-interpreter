package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class LogicalComparisonFunctionTestProvider {
    public static Stream<Arguments> provideIf() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), new Value("#NA!")),
                Arguments.of(new Value("#REF!"), new Value("#REF!")),
                Arguments.of(new Value("#NUM!"), new Value("#NUM!")),
                Arguments.of(new Value("#DIV/0!"), new Value("#DIV/0!")),
                Arguments.of(new Value("true"), new Value("true")),
                Arguments.of(new Value("false"), new Value("false")),
                Arguments.of(new Value("0"), new Value("false")),
                Arguments.of(new Value("1"), new Value("true"))
        );
    }

    public static Stream<Arguments> provideIfError() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), new Value("true")),
                Arguments.of(new Value("#REF!"), new Value("true")),
                Arguments.of(new Value("#NUM!"), new Value("true")),
                Arguments.of(new Value("#DIV/0!"), new Value("true")),
                Arguments.of(new Value("true"), new Value("false")),
                Arguments.of(new Value("false"), new Value("false")),
                Arguments.of(new Value("0"), new Value("false")),
                Arguments.of(new Value("1"), new Value("false")),
                Arguments.of(new Value("660"), new Value("false")),
                Arguments.of(new Value(""), new Value("false"))
        );
    }

    public static Stream<Arguments> provideIfNotAvailable() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), new Value("true")),
                Arguments.of(new Value("#REF!"), new Value("false")),
                Arguments.of(new Value("#NUM!"), new Value("false")),
                Arguments.of(new Value("#DIV/0!"), new Value("false")),
                Arguments.of(new Value("true"), new Value("false")),
                Arguments.of(new Value("false"), new Value("false")),
                Arguments.of(new Value("0"), new Value("false")),
                Arguments.of(new Value("1"), new Value("false")),
                Arguments.of(new Value("660"), new Value("false")),
                Arguments.of(new Value(""), new Value("false"))
        );
    }
}
