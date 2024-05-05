package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class FunctionTestProvider {
    public static Stream<Arguments> provideCommonResponses() {
        return Stream.of(
                Arguments.of(new Value("#NA!"), new Value("#NA!"), new Value("#NA!")),
                Arguments.of(new Value("#REF!"), new Value("#REF!"), new Value("#REF!")),
                Arguments.of(new Value("#NUM!"), new Value("#NUM!"), new Value("#NUM!")),
                Arguments.of(new Value("#DIV/0!"), new Value("#DIV/0!"), new Value("#DIV/0!"))
        );
    }
}
