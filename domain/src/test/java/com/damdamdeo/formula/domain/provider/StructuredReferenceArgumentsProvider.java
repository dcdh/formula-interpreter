package com.damdamdeo.formula.domain.provider;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.stream.Stream;

public final class StructuredReferenceArgumentsProvider implements ArgumentsProvider {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("RESOLVED")
    @ParameterizedTest
    @ArgumentsSource(StructuredReferenceArgumentsProvider.class)
    public @interface ResolvedTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("UNKNOWN")
    @ParameterizedTest
    @ArgumentsSource(StructuredReferenceArgumentsProvider.class)
    public @interface UnknownTest {
    }

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final StructuredReferenceProviders.State givenState = context.getTags().stream()
                .filter(StructuredReferenceProviders.State::matchTag)
                .map(StructuredReferenceProviders.State::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Should not be here: unknown state"));
        return StructuredReferenceProviders.byState(givenState)
                .stream()
                .map(reference -> Arguments.of(reference.reference(), reference.structuredReferences()));

    }
}
