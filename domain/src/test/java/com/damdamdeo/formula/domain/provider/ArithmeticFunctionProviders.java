package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public final class ArithmeticFunctionProviders {

    public enum Type {
        ADD, SUBTRACT, MULTIPLY, DIVIDE;

        public static boolean matchTag(final String tag) {
            for (final Type type : Type.values()) {
                if (type.name().equals(tag)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final Map<Type, List<ArithmeticFunction>> FUNCTIONS_BY_TYPE = new HashMap<>();

    public static List<ArithmeticFunction> byType(final Type type) {
        return new ArithmeticFunctionProviders().FUNCTIONS_BY_TYPE.get(type);
    }

    public ArithmeticFunctionProviders() {
        FUNCTIONS_BY_TYPE.put(
                Type.ADD,
                Stream.concat(
                        Stream.of(new ArithmeticFunction(new GivenLeft(new Value("660")), new GivenRight(new Value("260")), new Expected(new Value("920")))),
                        common()
                ).toList()
        );
        FUNCTIONS_BY_TYPE.put(
                Type.SUBTRACT,
                Stream.concat(
                        Stream.of(new ArithmeticFunction(new GivenLeft(new Value("660")), new GivenRight(new Value("260")), new Expected(new Value("400")))),
                        common()
                ).toList()
        );
        FUNCTIONS_BY_TYPE.put(
                Type.MULTIPLY,
                Stream.concat(
                        Stream.of(new ArithmeticFunction(new GivenLeft(new Value("660")), new GivenRight(new Value("260")), new Expected(new Value("171600")))),
                        common()
                ).toList()
        );
        FUNCTIONS_BY_TYPE.put(
                Type.DIVIDE,
                Stream.concat(
                        Stream.of(
                                new ArithmeticFunction(new GivenLeft(new Value("660")), new GivenRight(new Value("260")), new Expected(new Value("2.538462"))),
                                new ArithmeticFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofZero()), new Expected(Value.ofDividedByZero()))),
                        common()
                ).toList()
        );
    }

    private Stream<ArithmeticFunction> common() {
        return Stream.of(
                new ArithmeticFunction(new GivenLeft(new Value("azerty")), new GivenRight(new Value("260")), new Expected(Value.ofNumericalValueExpected())),
                new ArithmeticFunction(new GivenLeft(Value.ofTrue()), new GivenRight(new Value("260")), new Expected(Value.ofNumericalValueExpected())),
                new ArithmeticFunction(new GivenLeft(Value.ofFalse()), new GivenRight(new Value("260")), new Expected(Value.ofNumericalValueExpected())),
                new ArithmeticFunction(new GivenLeft(Value.ofNotAvailable()), new GivenRight(new Value("260")), new Expected(Value.ofNotAvailable())),
                new ArithmeticFunction(new GivenLeft(Value.ofUnknownRef()), new GivenRight(new Value("260")), new Expected(Value.ofUnknownRef())),
                new ArithmeticFunction(new GivenLeft(Value.ofNumericalValueExpected()), new GivenRight(new Value("260")), new Expected(Value.ofNumericalValueExpected())),
                new ArithmeticFunction(new GivenLeft(Value.ofDividedByZero()), new GivenRight(new Value("260")), new Expected(Value.ofDividedByZero())),
                new ArithmeticFunction(new GivenLeft(new Value("660")), new GivenRight(new Value("azerty")), new Expected(Value.ofNumericalValueExpected())),
                new ArithmeticFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofTrue()), new Expected(Value.ofNumericalValueExpected())),
                new ArithmeticFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofFalse()), new Expected(Value.ofNumericalValueExpected())),
                new ArithmeticFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofNotAvailable()), new Expected(Value.ofNotAvailable())),
                new ArithmeticFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofUnknownRef()), new Expected(Value.ofUnknownRef())),
                new ArithmeticFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofNumericalValueExpected()), new Expected(Value.ofNumericalValueExpected())),
                new ArithmeticFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofDividedByZero()), new Expected(Value.ofDividedByZero()))
        );
    }

    public record ArithmeticFunction(GivenLeft givenLeft, GivenRight givenRight, Expected expected) {
        public ArithmeticFunction {
            Objects.requireNonNull(givenLeft);
            Objects.requireNonNull(givenRight);
            Objects.requireNonNull(expected);
        }
    }
}
