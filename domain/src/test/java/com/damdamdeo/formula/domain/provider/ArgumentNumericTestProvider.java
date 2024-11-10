package com.damdamdeo.formula.domain.provider;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class ArgumentNumericTestProvider {

    public static Stream<Arguments> provideNumerical() {
        return Stream.of(
                Arguments.of(
                        "0",
                        "0.00",
                        "123",
                        "-123",
                        "1.23E3",
                        "1.23E+3",
                        "12.3E+7",
                        "12.0",
                        "12.3",
                        "0.00123",
                        "-1.23E-12",
                        "1234.5E-4",
                        "0E+7",
                        "-0")
        );
    }
}