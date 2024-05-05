package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class ArithmeticFunctionTestProvider {

    public static Stream<Arguments> provideAddition() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("920"))
        );
    }

    public static Stream<Arguments> provideSubtraction() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("400"))
        );
    }

    public static Stream<Arguments> provideDivision() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("2.538462")),
                Arguments.of(new Value("660"), new Value("0"), new Value("#DIV/0!"))
        );
    }

    public static Stream<Arguments> provideMultiplication() {
        return Stream.of(
                Arguments.of(new Value("660"), new Value("260"), new Value("171600"))
        );
    }

    public static Stream<Arguments> provideCommonResponses() {
        return Stream.of(
                Arguments.of(new Value("azerty"), new Value("260"), new Value("#NUM!")),
                Arguments.of(new Value("true"), new Value("260"), new Value("#NUM!")),
                Arguments.of(new Value("false"), new Value("260"), new Value("#NUM!")),
                Arguments.of(new Value("#NA!"), new Value("260"), new Value("#NA!")),
                Arguments.of(new Value("#REF!"), new Value("260"), new Value("#REF!")),
                Arguments.of(new Value("#NUM!"), new Value("260"), new Value("#NUM!")),
                Arguments.of(new Value("#DIV/0!"), new Value("260"), new Value("#DIV/0!")),
                Arguments.of(new Value("660"), new Value("azerty"), new Value("#NUM!")),
                Arguments.of(new Value("660"), new Value("true"), new Value("#NUM!")),
                Arguments.of(new Value("660"), new Value("false"), new Value("#NUM!")),
                Arguments.of(new Value("660"), new Value("#NA!"), new Value("#NA!")),
                Arguments.of(new Value("660"), new Value("#REF!"), new Value("#REF!")),
                Arguments.of(new Value("660"), new Value("#NUM!"), new Value("#NUM!")),
                Arguments.of(new Value("660"), new Value("#DIV/0!"), new Value("#DIV/0!"))
        );
    }

}
