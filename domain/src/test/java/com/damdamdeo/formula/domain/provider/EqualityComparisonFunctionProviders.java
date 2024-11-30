package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public final class EqualityComparisonFunctionProviders {

    public enum Type {
        EQUAL, NOT_EQUAL;

        public static boolean matchTag(final String tag) {
            for (final Type type : Type.values()) {
                if (type.name().equals(tag)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final Map<Type, List<EqualityComparisonFunction>> FUNCTIONS_BY_TYPE = new HashMap<>();

    public static List<EqualityComparisonFunction> byType(final Type type) {
        return new EqualityComparisonFunctionProviders().FUNCTIONS_BY_TYPE.get(type);
    }

    public EqualityComparisonFunctionProviders() {
        FUNCTIONS_BY_TYPE.put(
                Type.EQUAL,
                Stream.concat(
                        Stream.of(
                                new EqualityComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(new Value("260")), new Expected(Value.ofFalse())),
                                new EqualityComparisonFunction(new GivenLeft(new Value("260")), new GivenRight(new Value("660")), new Expected(Value.ofFalse())),
                                new EqualityComparisonFunction(new GivenLeft(new Value("260")), new GivenRight(new Value("260")), new Expected(Value.ofTrue())),
                                new EqualityComparisonFunction(new GivenLeft(new Value("toto")), new GivenRight(new Value("toto")), new Expected(Value.ofTrue())),
                                new EqualityComparisonFunction(new GivenLeft(new Value("tata")), new GivenRight(new Value("toto")), new Expected(Value.ofFalse())),
                                new EqualityComparisonFunction(new GivenLeft(Value.ofTrue()), new GivenRight(Value.ofTrue()), new Expected(Value.ofTrue())),
                                new EqualityComparisonFunction(new GivenLeft(Value.ofTrue()), new GivenRight(Value.ofFalse()), new Expected(Value.ofFalse())),
                                new EqualityComparisonFunction(new GivenLeft(Value.ofTrue()), new GivenRight(Value.ofTrue()), new Expected(Value.ofTrue())),
                                new EqualityComparisonFunction(new GivenLeft(Value.ofTrue()), new GivenRight(Value.ofFalse()), new Expected(Value.ofFalse()))),
                        common()
                ).toList()
        );
        FUNCTIONS_BY_TYPE.put(
                Type.NOT_EQUAL,
                Stream.concat(
                        Stream.of(
                                new EqualityComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(new Value("260")), new Expected(Value.ofTrue())),
                                new EqualityComparisonFunction(new GivenLeft(new Value("260")), new GivenRight(new Value("660")), new Expected(Value.ofTrue())),
                                new EqualityComparisonFunction(new GivenLeft(new Value("260")), new GivenRight(new Value("260")), new Expected(Value.ofFalse())),
                                new EqualityComparisonFunction(new GivenLeft(new Value("toto")), new GivenRight(new Value("toto")), new Expected(Value.ofFalse())),
                                new EqualityComparisonFunction(new GivenLeft(new Value("tata")), new GivenRight(new Value("toto")), new Expected(Value.ofTrue())),
                                new EqualityComparisonFunction(new GivenLeft(Value.ofTrue()), new GivenRight(Value.ofTrue()), new Expected(Value.ofFalse())),
                                new EqualityComparisonFunction(new GivenLeft(Value.ofTrue()), new GivenRight(Value.ofFalse()), new Expected(Value.ofTrue())),
                                new EqualityComparisonFunction(new GivenLeft(Value.ofTrue()), new GivenRight(Value.ofTrue()), new Expected(Value.ofFalse())),
                                new EqualityComparisonFunction(new GivenLeft(Value.ofTrue()), new GivenRight(Value.ofFalse()), new Expected(Value.ofTrue()))),
                        common()
                ).toList()
        );
    }

    private Stream<EqualityComparisonFunction> common() {
        return Stream.of(
                new EqualityComparisonFunction(new GivenLeft(Value.ofNotAvailable()), new GivenRight(new Value("260")), new Expected(Value.ofNotAvailable())),
                new EqualityComparisonFunction(new GivenLeft(Value.ofUnknownRef()), new GivenRight(new Value("260")), new Expected(Value.ofUnknownRef())),
                new EqualityComparisonFunction(new GivenLeft(Value.ofNumericalValueExpected()), new GivenRight(new Value("260")), new Expected(Value.ofNumericalValueExpected())),
                new EqualityComparisonFunction(new GivenLeft(Value.ofDividedByZero()), new GivenRight(new Value("260")), new Expected(Value.ofDividedByZero())),
                new EqualityComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofNotAvailable()), new Expected(Value.ofNotAvailable())),
                new EqualityComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofUnknownRef()), new Expected(Value.ofUnknownRef())),
                new EqualityComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofNumericalValueExpected()), new Expected(Value.ofNumericalValueExpected())),
                new EqualityComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofDividedByZero()), new Expected(Value.ofDividedByZero()))
        );
    }

    public record EqualityComparisonFunction(GivenLeft givenLeft, GivenRight givenRight, Expected expected) {
        public EqualityComparisonFunction {
            Objects.requireNonNull(givenLeft);
            Objects.requireNonNull(givenRight);
            Objects.requireNonNull(expected);
        }
    }
}
