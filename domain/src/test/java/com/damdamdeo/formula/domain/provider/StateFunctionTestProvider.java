package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class StateFunctionTestProvider {

    public static Stream<Arguments> provideIsNotAvailable() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), true),
                Arguments.of(new Value("#REF!"), false),
                Arguments.of(new Value("#NUM!"), false),
                Arguments.of(new Value("#DIV/0!"), false),
                Arguments.of(new Value("true"), false),
                Arguments.of(new Value("false"), false),
                Arguments.of(new Value("1"), false),
                Arguments.of(new Value("0"), false),
                Arguments.of(new Value("660"), false),
                Arguments.of(new Value("azerty"), false),
                Arguments.of(new Value(""), false)
        );
    }

    public static Stream<Arguments> provideIsError() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), true),
                Arguments.of(new Value("#REF!"), true),
                Arguments.of(new Value("#NUM!"), true),
                Arguments.of(new Value("#DIV/0!"), true),
                Arguments.of(new Value("true"), false),
                Arguments.of(new Value("false"), false),
                Arguments.of(new Value("1"), false),
                Arguments.of(new Value("0"), false),
                Arguments.of(new Value("660"), false),
                Arguments.of(new Value("azerty"), false),
                Arguments.of(new Value(""), false)
        );
    }

    public static Stream<Arguments> provideIsNumeric() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), false),
                Arguments.of(new Value("#REF!"), false),
                Arguments.of(new Value("#NUM!"), false),
                Arguments.of(new Value("#DIV/0!"), false),
                Arguments.of(new Value("true"), false),
                Arguments.of(new Value("false"), false),
                Arguments.of(new Value("1"), true),
                Arguments.of(new Value("0"), true),
                Arguments.of(new Value("660"), true),
                Arguments.of(new Value("azerty"), false),
                Arguments.of(new Value(""), false)
        );
    }

    public static Stream<Arguments> provideIsText() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), false),
                Arguments.of(new Value("#REF!"), false),
                Arguments.of(new Value("#NUM!"), false),
                Arguments.of(new Value("#DIV/0!"), false),
                Arguments.of(new Value("true"), false),
                Arguments.of(new Value("false"), false),
                Arguments.of(new Value("1"), false),
                Arguments.of(new Value("0"), false),
                Arguments.of(new Value("660"), false),
                Arguments.of(new Value("azerty"), true),
                Arguments.of(new Value(""), true)
        );
    }

    public static Stream<Arguments> provideIsBlank() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), false),
                Arguments.of(new Value("#REF!"), false),
                Arguments.of(new Value("#NUM!"), false),
                Arguments.of(new Value("#DIV/0!"), false),
                Arguments.of(new Value("true"), false),
                Arguments.of(new Value("false"), false),
                Arguments.of(new Value("1"), false),
                Arguments.of(new Value("0"), false),
                Arguments.of(new Value("660"), false),
                Arguments.of(new Value("azerty"), false),
                Arguments.of(new Value(""), true)
        );
    }

    public static Stream<Arguments> provideIsLogical() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), false),
                Arguments.of(new Value("#REF!"), false),
                Arguments.of(new Value("#NUM!"), false),
                Arguments.of(new Value("#DIV/0!"), false),
                Arguments.of(new Value("true"), true),
                Arguments.of(new Value("false"), true),
                Arguments.of(new Value("1"), true),
                Arguments.of(new Value("0"), true),
                Arguments.of(new Value("660"), false),
                Arguments.of(new Value("azerty"), false),
                Arguments.of(new Value(""), false)
        );
    }
}
