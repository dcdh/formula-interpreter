package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;
import com.damdamdeo.formula.domain.ValueTest;

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
                                new NumericalComparisonFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofTrue())),
                                new NumericalComparisonFunction(new GivenLeft(ValueTest.TWO_SIX_ZERO), new GivenRight(ValueTest.SIX_SIX_ZERO), new Expected(Value.ofFalse())),
                                new NumericalComparisonFunction(new GivenLeft(ValueTest.TWO_SIX_ZERO), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofFalse()))),
                        common()
                ).toList()
        );
        FUNCTIONS_BY_TYPE.put(
                Type.GREATER_THAN_OR_EQUAL_TO,
                Stream.concat(
                        Stream.of(
                                new NumericalComparisonFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofTrue())),
                                new NumericalComparisonFunction(new GivenLeft(ValueTest.TWO_SIX_ZERO), new GivenRight(ValueTest.SIX_SIX_ZERO), new Expected(Value.ofFalse())),
                                new NumericalComparisonFunction(new GivenLeft(ValueTest.TWO_SIX_ZERO), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofTrue()))),
                        common()
                ).toList()
        );
        FUNCTIONS_BY_TYPE.put(
                Type.LESS_THAN,
                Stream.concat(
                        Stream.of(
                                new NumericalComparisonFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofFalse())),
                                new NumericalComparisonFunction(new GivenLeft(ValueTest.TWO_SIX_ZERO), new GivenRight(ValueTest.SIX_SIX_ZERO), new Expected(Value.ofTrue())),
                                new NumericalComparisonFunction(new GivenLeft(ValueTest.TWO_SIX_ZERO), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofFalse()))),
                        common()
                ).toList()
        );
        FUNCTIONS_BY_TYPE.put(
                Type.LESS_THAN_OR_EQUAL_TO,
                Stream.concat(
                        Stream.of(
                                new NumericalComparisonFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofFalse())),
                                new NumericalComparisonFunction(new GivenLeft(ValueTest.TWO_SIX_ZERO), new GivenRight(ValueTest.SIX_SIX_ZERO), new Expected(Value.ofTrue())),
                                new NumericalComparisonFunction(new GivenLeft(ValueTest.TWO_SIX_ZERO), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofTrue()))),
                        common()
                ).toList()
        );
    }

    private Stream<NumericalComparisonFunction> common() {
        return Stream.of(
                new NumericalComparisonFunction(new GivenLeft(Value.ofNotAvailable()), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofNotAvailable())),
                new NumericalComparisonFunction(new GivenLeft(Value.ofUnknownRef()), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofUnknownRef())),
                new NumericalComparisonFunction(new GivenLeft(Value.ofNotANumericalValue()), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofNotANumericalValue())),
                new NumericalComparisonFunction(new GivenLeft(Value.ofDividedByZero()), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofDividedByZero())),
                new NumericalComparisonFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(Value.ofNotAvailable()), new Expected(Value.ofNotAvailable())),
                new NumericalComparisonFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(Value.ofUnknownRef()), new Expected(Value.ofUnknownRef())),
                new NumericalComparisonFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(Value.ofNotANumericalValue()), new Expected(Value.ofNotANumericalValue())),
                new NumericalComparisonFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(Value.ofDividedByZero()), new Expected(Value.ofDividedByZero())),
                new NumericalComparisonFunction(new GivenLeft(ValueTest.AZERTY), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofNotANumericalValue())),
                new NumericalComparisonFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(ValueTest.AZERTY), new Expected(Value.ofNotANumericalValue())));
    }

    public record NumericalComparisonFunction(GivenLeft givenLeft, GivenRight givenRight, Expected expected) {
        public NumericalComparisonFunction {
            Objects.requireNonNull(givenLeft);
            Objects.requireNonNull(givenRight);
            Objects.requireNonNull(expected);
        }
    }

}
