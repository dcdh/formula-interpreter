package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public final class NumericalComparisonFunctionProviders {

    public enum Type {
        GREATER_THAN, GREATER_THAN_OR_EQUAL_TO, LESS_THAN, LESS_THAN_OR_EQUAL_TO;

        public static boolean matchTag(final String tag) {
            for (final Type type : Type.values()) {
                if (type.name().equals(tag)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final Map<Type, List<NumericalComparisonFunction>> FUNCTIONS_BY_TYPE = new HashMap<>();

    public static List<NumericalComparisonFunction> byType(final Type type) {
        return new NumericalComparisonFunctionProviders().FUNCTIONS_BY_TYPE.get(type);
    }

    public NumericalComparisonFunctionProviders() {
        FUNCTIONS_BY_TYPE.put(
                Type.GREATER_THAN,
                Stream.concat(
                        Stream.of(
                                new NumericalComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(new Value("260")), new Expected(Value.ofTrue())),
                                new NumericalComparisonFunction(new GivenLeft(new Value("260")), new GivenRight(new Value("660")), new Expected(Value.ofFalse())),
                                new NumericalComparisonFunction(new GivenLeft(new Value("260")), new GivenRight(new Value("260")), new Expected(Value.ofFalse()))),
                        common()
                ).toList()
        );
        FUNCTIONS_BY_TYPE.put(
                Type.GREATER_THAN_OR_EQUAL_TO,
                Stream.concat(
                        Stream.of(
                                new NumericalComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(new Value("260")), new Expected(Value.ofTrue())),
                                new NumericalComparisonFunction(new GivenLeft(new Value("260")), new GivenRight(new Value("660")), new Expected(Value.ofFalse())),
                                new NumericalComparisonFunction(new GivenLeft(new Value("260")), new GivenRight(new Value("260")), new Expected(Value.ofTrue()))),
                        common()
                ).toList()
        );
        FUNCTIONS_BY_TYPE.put(
                Type.LESS_THAN,
                Stream.concat(
                        Stream.of(
                                new NumericalComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(new Value("260")), new Expected(Value.ofFalse())),
                                new NumericalComparisonFunction(new GivenLeft(new Value("260")), new GivenRight(new Value("660")), new Expected(Value.ofTrue())),
                                new NumericalComparisonFunction(new GivenLeft(new Value("260")), new GivenRight(new Value("260")), new Expected(Value.ofFalse()))),
                        common()
                ).toList()
        );
        FUNCTIONS_BY_TYPE.put(
                Type.LESS_THAN_OR_EQUAL_TO,
                Stream.concat(
                        Stream.of(
                                new NumericalComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(new Value("260")), new Expected(Value.ofFalse())),
                                new NumericalComparisonFunction(new GivenLeft(new Value("260")), new GivenRight(new Value("660")), new Expected(Value.ofTrue())),
                                new NumericalComparisonFunction(new GivenLeft(new Value("260")), new GivenRight(new Value("260")), new Expected(Value.ofTrue()))),
                        common()
                ).toList()
        );
    }

    private Stream<NumericalComparisonFunction> common() {
        return Stream.of(
                new NumericalComparisonFunction(new GivenLeft(Value.ofNotAvailable()), new GivenRight(new Value("260")), new Expected(Value.ofNotAvailable())),
                new NumericalComparisonFunction(new GivenLeft(Value.ofUnknownRef()), new GivenRight(new Value("260")), new Expected(Value.ofUnknownRef())),
                new NumericalComparisonFunction(new GivenLeft(Value.ofNumericalValueExpected()), new GivenRight(new Value("260")), new Expected(Value.ofNumericalValueExpected())),
                new NumericalComparisonFunction(new GivenLeft(Value.ofDividedByZero()), new GivenRight(new Value("260")), new Expected(Value.ofDividedByZero())),
                new NumericalComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofNotAvailable()), new Expected(Value.ofNotAvailable())),
                new NumericalComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofUnknownRef()), new Expected(Value.ofUnknownRef())),
                new NumericalComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofNumericalValueExpected()), new Expected(Value.ofNumericalValueExpected())),
                new NumericalComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(Value.ofDividedByZero()), new Expected(Value.ofDividedByZero())),
                new NumericalComparisonFunction(new GivenLeft(new Value("azerty")), new GivenRight(new Value("260")), new Expected(Value.ofNumericalValueExpected())),
                new NumericalComparisonFunction(new GivenLeft(new Value("660")), new GivenRight(new Value("azerty")), new Expected(Value.ofNumericalValueExpected())));
    }

    public record NumericalComparisonFunction(GivenLeft givenLeft, GivenRight givenRight, Expected expected) {
        public NumericalComparisonFunction {
            Objects.requireNonNull(givenLeft);
            Objects.requireNonNull(givenRight);
            Objects.requireNonNull(expected);
        }
    }

}
