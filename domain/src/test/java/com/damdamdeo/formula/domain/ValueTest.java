package com.damdamdeo.formula.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class ValueTest {
    @Nested
    class IsNumeric {

        @ParameterizedTest
        @MethodSource("com.damdamdeo.formula.domain.provider.ArgumentNumericTestProvider#provideNumerical")
        public void shouldBeANumeric(final String givenValue) {
            assertThat(new Value(givenValue).isNumeric()).isTrue();
        }

        @Test
        public void shouldNotAvailableNotBeANumeric() {
            assertThat(Value.ofNotAvailable().isNumeric()).isFalse();
        }

        @Test
        public void shouldUnknownRefNotBeANumericTest() {
            assertThat(Value.ofUnknownRef().isNumeric()).isFalse();
        }

        @Test
        public void shouldNotANumericalValueNotBeANumericTest() {
            assertThat(Value.ofNumericalValueExpected().isNumeric()).isFalse();
        }

        @Test
        public void shouldDivByZeroNotBeANumericTest() {
            assertThat(Value.ofDividedByZero().isNumeric()).isFalse();
        }

        @Test
        public void shouldNotALogicalValueNotBeANumericTest() {
            assertThat(Value.ofLogicalValueExpected().isNumeric()).isFalse();
        }

        @Test
        public void shouldTrueNotBeANumericTest() {
            assertThat(Value.ofTrue().isNumeric()).isFalse();
        }

        @Test
        public void shouldFalseNotBeANumericTest() {
            assertThat(Value.ofFalse().isNumeric()).isFalse();
        }

        @Test
        public void shouldAzertyNotBeANumericTest() {
            assertThat(new Value("Azerty").isNumeric()).isFalse();
        }

        @Test
        public void shouldHelloSpaceWorldNotBeANumericTest() {
            assertThat(new Value("Hello World").isNumeric()).isFalse();
        }
    }
    // FCK lot of things to implements !
}
