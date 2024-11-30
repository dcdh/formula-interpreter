package com.damdamdeo.formula.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ArgumentTest {

    @Test
    void shouldResolveTextArgument() {
        // Given
        final Argument argument = Argument.ofText(Value.ofText("Hello World"));

        // When
        final Value value = argument.resolveArgument(List.of());

        // Then
        assertAll(
                () -> assertThat(argument).isEqualTo(new Argument(Argument.Kind.TEXT, Value.ofText("Hello World"), null)),
                () -> assertThat(value).isEqualTo(Value.ofText("Hello World"))
        );
    }

    @Test
    void shouldResolveNumericArgument() {
        // Given
        final Argument argument = Argument.ofNumeric(Value.ofNumeric("660"));

        // When
        final Value value = argument.resolveArgument(List.of());

        // Then
        assertAll(
                () -> assertThat(argument).isEqualTo(new Argument(Argument.Kind.NUMERIC, Value.ofNumeric("660"), null)),
                () -> assertThat(value).isEqualTo(Value.ofNumeric("660"))
        );
    }

    @Test
    void shouldResolveBooleanArgument() {
        // Given
        final Argument argument = Argument.ofBoolean(Value.ofFalse());

        // When
        final Value value = argument.resolveArgument(List.of());

        // Then
        assertAll(
                () -> assertThat(argument).isEqualTo(new Argument(Argument.Kind.BOOLEAN, Value.ofFalse(), null)),
                () -> assertThat(value).isEqualTo(Value.ofFalse())
        );
    }

    @Test
    void shouldResolveStructuredReferenceWhenPresent() {
        // Given
        final Argument argument = Argument.ofStructuredReference(new Reference("[@[% Commission]]"));

        // When
        final Value value = argument.resolveArgument(List.of(
                new StructuredReference(
                        new ReferenceNaming("% Commission"),
                        Value.ofNumeric("0.10")
                )));

        // Then
        assertAll(
                () -> assertThat(argument).isEqualTo(new Argument(Argument.Kind.STRUCTURED_REFERENCE, null, new Reference("[@[% Commission]]"))),
                () -> assertThat(value).isEqualTo(Value.ofNumeric("0.10"))
        );
    }

    @Test
    void shouldReturnUnknownRefWhenStructuredReferenceIsNotPresent() {
        // Given
        final Argument argument = Argument.ofStructuredReference(new Reference("[@[% Commission]]"));

        // When
        final Value value = argument.resolveArgument(List.of());

        // Then
        assertAll(
                () -> assertThat(argument).isEqualTo(new Argument(Argument.Kind.STRUCTURED_REFERENCE, null, new Reference("[@[% Commission]]"))),
                () -> assertThat(value).isEqualTo(Value.ofUnknownRef())
        );
    }
}