package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Value;

import java.util.List;
import java.util.Objects;

public final class BiFunctionCommonProviders {

    public static List<BiFunction> provide() {
        return List.of(
                new BiFunction(new GivenLeft(Value.ofNotAvailable()), new GivenRight(Value.ofNotAvailable()), new Expected(Value.ofNotAvailable())),
                new BiFunction(new GivenLeft(Value.ofUnknownRef()), new GivenRight(Value.ofUnknownRef()), new Expected(Value.ofUnknownRef())),
                new BiFunction(new GivenLeft(Value.ofNumericalValueExpected()), new GivenRight(Value.ofNumericalValueExpected()), new Expected(Value.ofNumericalValueExpected())),
                new BiFunction(new GivenLeft(Value.ofDividedByZero()), new GivenRight(Value.ofDividedByZero()), new Expected(Value.ofDividedByZero()))
        );
    }

    public record BiFunction(GivenLeft givenLeft, GivenRight givenRight, Expected expected) {
        public BiFunction {
            Objects.requireNonNull(givenLeft);
            Objects.requireNonNull(givenRight);
            Objects.requireNonNull(expected);
        }
    }

}
