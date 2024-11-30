package com.damdamdeo.formula.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReferenceTest {

    @Test
    void shouldReturnNaming() {
        // Given

        // When
        final Reference reference = new Reference("[@[% Commission]]");

        // Then
        assertAll(
                () -> assertThat(reference.reference()).isEqualTo("[@[% Commission]]"),
                () -> assertThat(reference.value()).isEqualTo("[@[% Commission]]"),
                () -> assertThat(reference.toReferenceNaming()).isEqualTo(new ReferenceNaming("% Commission"))
        );
    }
}