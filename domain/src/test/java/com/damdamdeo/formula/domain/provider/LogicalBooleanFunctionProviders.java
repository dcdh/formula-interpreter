package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;
import com.damdamdeo.formula.domain.ValueTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public final class LogicalBooleanFunctionProviders {

    public enum Type {
        OR, AND;

        public static boolean matchTag(final String tag) {
            for (final Type type : Type.values()) {
                if (type.name().equals(tag)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final Map<Type, List<LogicalBooleanFunction>> FUNCTIONS_BY_TYPE = new HashMap<>();

    public static List<LogicalBooleanFunction> byType(final Type type) {
        return new LogicalBooleanFunctionProviders().FUNCTIONS_BY_TYPE.get(type);
    }

    public LogicalBooleanFunctionProviders() {
        
        FUNCTIONS_BY_TYPE.put(
                Type.OR,
                Stream.concat(
                        Stream.of(
                                new LogicalBooleanFunction(new GivenLeft(Value.ofZero()), new GivenRight(Value.ofOne()), new Expected(Value.ofTrue())),
                                new LogicalBooleanFunction(new GivenLeft(Value.ofOne()), new GivenRight(Value.ofZero()), new Expected(Value.ofTrue())),
                                new LogicalBooleanFunction(new GivenLeft(Value.ofZero()), new GivenRight(Value.ofTrue()), new Expected(Value.ofTrue()))
                        ),
                        common()
                ).toList()
        );
        FUNCTIONS_BY_TYPE.put(
                Type.AND,
                Stream.concat(
                        Stream.of(
                                new LogicalBooleanFunction(new GivenLeft(Value.ofZero()), new GivenRight(Value.ofOne()), new Expected(Value.ofFalse())),
                                new LogicalBooleanFunction(new GivenLeft(Value.ofOne()), new GivenRight(Value.ofZero()), new Expected(Value.ofFalse())),
                                new LogicalBooleanFunction(new GivenLeft(Value.ofZero()), new GivenRight(Value.ofTrue()), new Expected(Value.ofFalse()))
                        ),
                        common()
                ).toList()
        );
    }

    private Stream<LogicalBooleanFunction> common() {
        return Stream.of(
                new LogicalBooleanFunction(new GivenLeft(Value.ofZero()), new GivenRight(Value.ofZero()), new Expected(Value.ofFalse())),
                new LogicalBooleanFunction(new GivenLeft(Value.ofOne()), new GivenRight(Value.ofOne()), new Expected(Value.ofTrue())),
                new LogicalBooleanFunction(new GivenLeft(Value.ofOne()), new GivenRight(Value.ofTrue()), new Expected(Value.ofTrue())),
                new LogicalBooleanFunction(new GivenLeft(Value.ofTrue()), new GivenRight(Value.ofOne()), new Expected(Value.ofTrue())),
                new LogicalBooleanFunction(new GivenLeft(Value.ofTrue()), new GivenRight(Value.ofTrue()), new Expected(Value.ofTrue())),
                new LogicalBooleanFunction(new GivenLeft(ValueTest.AZERTY), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofNotALogicalValue())),
                new LogicalBooleanFunction(new GivenLeft(Value.ofTrue()), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofNotALogicalValue())),
                new LogicalBooleanFunction(new GivenLeft(Value.ofFalse()), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofNotALogicalValue())),
                new LogicalBooleanFunction(new GivenLeft(Value.ofNotAvailable()), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofNotAvailable())),
                new LogicalBooleanFunction(new GivenLeft(Value.ofUnknownRef()), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofUnknownRef())),
                new LogicalBooleanFunction(new GivenLeft(Value.ofNotANumericalValue()), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofNotANumericalValue())),
                new LogicalBooleanFunction(new GivenLeft(Value.ofDividedByZero()), new GivenRight(ValueTest.TWO_SIX_ZERO), new Expected(Value.ofDividedByZero())),
                new LogicalBooleanFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(ValueTest.AZERTY), new Expected(Value.ofNotALogicalValue())),
                new LogicalBooleanFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(Value.ofTrue()), new Expected(Value.ofNotALogicalValue())),
                new LogicalBooleanFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(Value.ofFalse()), new Expected(Value.ofNotALogicalValue())),
                new LogicalBooleanFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(Value.ofNotAvailable()), new Expected(Value.ofNotAvailable())),
                new LogicalBooleanFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(Value.ofUnknownRef()), new Expected(Value.ofUnknownRef())),
                new LogicalBooleanFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(Value.ofNotANumericalValue()), new Expected(Value.ofNotANumericalValue())),
                new LogicalBooleanFunction(new GivenLeft(ValueTest.SIX_SIX_ZERO), new GivenRight(Value.ofDividedByZero()), new Expected(Value.ofDividedByZero()))
        );
    }

    public record LogicalBooleanFunction(GivenLeft givenLeft, GivenRight givenRight, Expected expected) {
        public LogicalBooleanFunction {
            Objects.requireNonNull(givenLeft);
            Objects.requireNonNull(givenRight);
            Objects.requireNonNull(expected);
        }
    }

}
