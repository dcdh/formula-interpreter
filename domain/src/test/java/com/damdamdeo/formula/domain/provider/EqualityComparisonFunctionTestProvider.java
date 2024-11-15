package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;
import org.junit.jupiter.params.provider.Arguments;

import java.util.function.Function;
import java.util.stream.Stream;

public class EqualityComparisonFunctionTestProvider {

    public static Stream<Arguments> provideEqual() {
        return Stream.concat(
                Stream.of(
                        Arguments.of(new Value("660"), new Value("260"), new Value("false")),
                        Arguments.of(new Value("260"), new Value("660"), new Value("false")),
                        Arguments.of(new Value("260"), new Value("260"), new Value("true")),
                        Arguments.of(new Value("toto"), new Value("toto"), new Value("true")),
                        Arguments.of(new Value("tata"), new Value("toto"), new Value("false")),
                        Arguments.of(new Value("true"), new Value("true"), new Value("true")),
                        Arguments.of(new Value("true"), new Value("false"), new Value("false")),
                        Arguments.of(new Value("true"), new Value("true"), new Value("true")),
                        Arguments.of(new Value("true"), new Value("false"), new Value("false"))
                ),
                provideCommonResponses()
        );
    }

    public static Stream<Arguments> provideNotEqual() {
        return Stream.concat(
                Stream.of(
                        Arguments.of(new Value("660"), new Value("260"), new Value("true")),
                        Arguments.of(new Value("260"), new Value("660"), new Value("true")),
                        Arguments.of(new Value("260"), new Value("260"), new Value("false")),
                        Arguments.of(new Value("toto"), new Value("toto"), new Value("false")),
                        Arguments.of(new Value("tata"), new Value("toto"), new Value("true")),
                        Arguments.of(new Value("true"), new Value("true"), new Value("false")),
                        Arguments.of(new Value("true"), new Value("false"), new Value("true")),
                        Arguments.of(new Value("true"), new Value("true"), new Value("false")),
                        Arguments.of(new Value("true"), new Value("false"), new Value("true"))
                ),
                provideCommonResponses()
        );
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
                        Arguments.of(new Value("660"), new Value("#DIV/0!"), new Value("#DIV/0!"))
                ),
                FunctionTestProvider.provideCommonResponses()
        );
    }

}
