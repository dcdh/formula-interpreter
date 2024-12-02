package com.damdamdeo.formula.domain;

import com.damdamdeo.formula.domain.provider.GivenValue;
import com.damdamdeo.formula.domain.provider.StructuredReferenceArgumentsProvider;
import com.damdamdeo.formula.domain.provider.ValueArgumentsProvider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

// TODO rename : not Argument but Value
class ArgumentTest {

    @ValueArgumentsProvider.TextTest
    void shouldResolveTextArgument(final GivenValue givenValue) {
        // Given
        final Argument argument = Argument.ofText(givenValue.value());

        // When
        final Value value = argument.resolveArgument(List.of());

        // Then
        assertAll(
                () -> assertThat(argument).isEqualTo(new Argument(Argument.Kind.TEXT, givenValue.value(), null)),
                () -> assertThat(value).isEqualTo(givenValue.value())
        );
    }

    @ValueArgumentsProvider.NumericTest
    void shouldResolveNumericArgument(final GivenValue givenValue) {
        // Given
        final Argument argument = Argument.ofNumeric(givenValue.value());

        // When
        final Value value = argument.resolveArgument(List.of());

        // Then
        assertAll(
                () -> assertThat(argument).isEqualTo(new Argument(Argument.Kind.NUMERIC, givenValue.value(), null)),
                () -> assertThat(value).isEqualTo(givenValue.value())
        );
    }

    @ValueArgumentsProvider.BooleanTrueTest
    void shouldResolveBooleanTrueArgument(final GivenValue givenValue) {
        // Given
        final Argument argument = Argument.ofBoolean(givenValue.value());

        // When
        final Value value = argument.resolveArgument(List.of());

        // Then
        assertAll(
                () -> assertThat(argument).isEqualTo(new Argument(Argument.Kind.BOOLEAN, Value.ofTrue(), null)),
                () -> assertThat(value).isEqualTo(Value.ofTrue())
        );
    }

    @ValueArgumentsProvider.BooleanFalseTest
    void shouldResolveBooleanFalseArgument(final GivenValue givenValue) {
        // Given
        final Argument argument = Argument.ofBoolean(givenValue.value());

        // When
        final Value value = argument.resolveArgument(List.of());

        // Then
        assertAll(
                () -> assertThat(argument).isEqualTo(new Argument(Argument.Kind.BOOLEAN, Value.ofFalse(), null)),
                () -> assertThat(value).isEqualTo(Value.ofFalse())
        );
    }

    @StructuredReferenceArgumentsProvider.ResolvedTest
    void shouldResolveStructuredReferenceWhenPresent(final Reference reference, final List<StructuredReference> structuredReferences) {
        // Given
        final Argument argument = Argument.ofStructuredReference(reference);

        // When
        final Value value = argument.resolveArgument(structuredReferences);

        // Then
        assertAll(
                () -> assertThat(argument).isEqualTo(new Argument(Argument.Kind.STRUCTURED_REFERENCE, null, new Reference("[@[% Commission]]"))),
                () -> assertThat(value).isEqualTo(Value.ofNumeric("0.10"))
        );
    }

    @StructuredReferenceArgumentsProvider.UnknownTest
    void shouldReturnUnknownRefWhenStructuredReferenceIsNotPresent(final Reference reference, final List<StructuredReference> structuredReferences) {
        // Given
        final Argument argument = Argument.ofStructuredReference(reference);

        // When
        final Value value = argument.resolveArgument(structuredReferences);

        // Then
        assertAll(
                () -> assertThat(argument).isEqualTo(new Argument(Argument.Kind.STRUCTURED_REFERENCE, null, new Reference("[@[% Commission]]"))),
                () -> assertThat(value).isEqualTo(Value.ofUnknownRef())
        );
    }
}