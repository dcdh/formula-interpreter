package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class LogicalBooleanFunctionTestProvider {
    public static Stream<Arguments> provideOr() {
        return Stream.of(
                Arguments.of(new Value("0"), new Value("0"), new Value("false")),
                Arguments.of(new Value("0"), new Value("1"), new Value("true")),
                Arguments.of(new Value("1"), new Value("0"), new Value("true")),
                Arguments.of(new Value("1"), new Value("1"), new Value("true")),
                Arguments.of(new Value("1"), new Value("true"), new Value("true")),
                Arguments.of(new Value("true"), new Value("1"), new Value("true")),
                Arguments.of(new Value("true"), new Value("true"), new Value("true")),
                Arguments.of(new Value("0"), new Value("true"), new Value("true"))
        );
    }

    public static Stream<Arguments> provideAnd() {
        return Stream.of(
                Arguments.of(new Value("0"), new Value("0"), new Value("false")),
                Arguments.of(new Value("0"), new Value("1"), new Value("false")),
                Arguments.of(new Value("1"), new Value("0"), new Value("false")),
                Arguments.of(new Value("1"), new Value("1"), new Value("true")),
                Arguments.of(new Value("1"), new Value("true"), new Value("true")),
                Arguments.of(new Value("true"), new Value("1"), new Value("true")),
                Arguments.of(new Value("true"), new Value("true"), new Value("true")),
                Arguments.of(new Value("0"), new Value("true"), new Value("false"))
        );
    }

    public static Stream<Arguments> provideCommonResponses() {
        return Stream.of(
                Arguments.of(new Value("azerty"), new Value("260"), new Value("#LOG!")),
                Arguments.of(new Value("true"), new Value("260"), new Value("#LOG!")),
                Arguments.of(new Value("false"), new Value("260"), new Value("#LOG!")),
                Arguments.of(new Value("#NA!"), new Value("260"), new Value("#NA!")),
                Arguments.of(new Value("#REF!"), new Value("260"), new Value("#REF!")),
                Arguments.of(new Value("#NUM!"), new Value("260"), new Value("#NUM!")),
                Arguments.of(new Value("#DIV/0!"), new Value("260"), new Value("#DIV/0!")),
                Arguments.of(new Value("660"), new Value("azerty"), new Value("#LOG!")),
                Arguments.of(new Value("660"), new Value("true"), new Value("#LOG!")),
                Arguments.of(new Value("660"), new Value("false"), new Value("#LOG!")),
                Arguments.of(new Value("660"), new Value("#NA!"), new Value("#NA!")),
                Arguments.of(new Value("660"), new Value("#REF!"), new Value("#REF!")),
                Arguments.of(new Value("660"), new Value("#NUM!"), new Value("#NUM!")),
                Arguments.of(new Value("660"), new Value("#DIV/0!"), new Value("#DIV/0!"))
        );
    }

}
