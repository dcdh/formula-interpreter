package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class StateFunctionTestProvider {

    public static Stream<Arguments> provideIsNotAvailable() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), Value.ofTrue()),
                Arguments.of(new Value("#REF!"), Value.ofFalse()),
                Arguments.of(new Value("#NUM!"), Value.ofFalse()),
                Arguments.of(new Value("#DIV/0!"), Value.ofFalse()),
                Arguments.of(new Value("true"), Value.ofFalse()),
                Arguments.of(new Value("false"), Value.ofFalse()),
                Arguments.of(new Value("1"), Value.ofFalse()),
                Arguments.of(new Value("0"), Value.ofFalse()),
                Arguments.of(new Value("660"), Value.ofFalse()),
                Arguments.of(new Value("azerty"), Value.ofFalse()),
                Arguments.of(new Value(""), Value.ofFalse())
        );
    }

    public static Stream<Arguments> provideIsError() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), Value.ofTrue()),
                Arguments.of(new Value("#REF!"), Value.ofTrue()),
                Arguments.of(new Value("#NUM!"), Value.ofTrue()),
                Arguments.of(new Value("#DIV/0!"), Value.ofTrue()),
                Arguments.of(new Value("true"), Value.ofFalse()),
                Arguments.of(new Value("false"), Value.ofFalse()),
                Arguments.of(new Value("1"), Value.ofFalse()),
                Arguments.of(new Value("0"), Value.ofFalse()),
                Arguments.of(new Value("660"), Value.ofFalse()),
                Arguments.of(new Value("azerty"), Value.ofFalse()),
                Arguments.of(new Value(""), Value.ofFalse())
        );
    }

    public static Stream<Arguments> provideIsNumeric() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), Value.ofFalse()),
                Arguments.of(new Value("#REF!"), Value.ofFalse()),
                Arguments.of(new Value("#NUM!"), Value.ofFalse()),
                Arguments.of(new Value("#DIV/0!"), Value.ofFalse()),
                Arguments.of(new Value("true"), Value.ofFalse()),
                Arguments.of(new Value("false"), Value.ofFalse()),
                Arguments.of(new Value("1"), Value.ofTrue()),
                Arguments.of(new Value("0"), Value.ofTrue()),
                Arguments.of(new Value("660"), Value.ofTrue()),
                Arguments.of(new Value("azerty"), Value.ofFalse()),
                Arguments.of(new Value(""), Value.ofFalse())
        );
    }

    public static Stream<Arguments> provideIsText() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), Value.ofFalse()),
                Arguments.of(new Value("#REF!"), Value.ofFalse()),
                Arguments.of(new Value("#NUM!"), Value.ofFalse()),
                Arguments.of(new Value("#DIV/0!"), Value.ofFalse()),
                Arguments.of(new Value("true"), Value.ofFalse()),
                Arguments.of(new Value("false"), Value.ofFalse()),
                Arguments.of(new Value("1"), Value.ofFalse()),
                Arguments.of(new Value("0"), Value.ofFalse()),
                Arguments.of(new Value("660"), Value.ofFalse()),
                Arguments.of(new Value("azerty"), Value.ofTrue()),
                Arguments.of(new Value(""), Value.ofTrue())
        );
    }

    public static Stream<Arguments> provideIsBlank() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), Value.ofFalse()),
                Arguments.of(new Value("#REF!"), Value.ofFalse()),
                Arguments.of(new Value("#NUM!"), Value.ofFalse()),
                Arguments.of(new Value("#DIV/0!"), Value.ofFalse()),
                Arguments.of(new Value("true"), Value.ofFalse()),
                Arguments.of(new Value("false"), Value.ofFalse()),
                Arguments.of(new Value("1"), Value.ofFalse()),
                Arguments.of(new Value("0"), Value.ofFalse()),
                Arguments.of(new Value("660"), Value.ofFalse()),
                Arguments.of(new Value("azerty"), Value.ofFalse()),
                Arguments.of(new Value(""), Value.ofTrue())
        );
    }

    public static Stream<Arguments> provideIsLogical() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), Value.ofFalse()),
                Arguments.of(new Value("#REF!"), Value.ofFalse()),
                Arguments.of(new Value("#NUM!"), Value.ofFalse()),
                Arguments.of(new Value("#DIV/0!"), Value.ofFalse()),
                Arguments.of(new Value("true"), Value.ofTrue()),
                Arguments.of(new Value("false"), Value.ofTrue()),
                Arguments.of(new Value("1"), Value.ofTrue()),
                Arguments.of(new Value("0"), Value.ofTrue()),
                Arguments.of(new Value("660"), Value.ofFalse()),
                Arguments.of(new Value("azerty"), Value.ofFalse()),
                Arguments.of(new Value(""), Value.ofFalse())
        );
    }
}
