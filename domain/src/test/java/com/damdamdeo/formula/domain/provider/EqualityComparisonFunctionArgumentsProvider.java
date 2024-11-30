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

public final class EqualityComparisonFunctionArgumentsProvider implements ArgumentsProvider {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("EQUAL")
    @ParameterizedTest
    @ArgumentsSource(EqualityComparisonFunctionArgumentsProvider.class)
    public @interface EqualTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("NOT_EQUAL")
    @ParameterizedTest
    @ArgumentsSource(EqualityComparisonFunctionArgumentsProvider.class)
    public @interface NotEqualTest {
    }

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final EqualityComparisonFunctionProviders.Type givenType = context.getTags().stream()
                .filter(EqualityComparisonFunctionProviders.Type::matchTag)
                .map(EqualityComparisonFunctionProviders.Type::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Should not be here: unknown type"));
        return EqualityComparisonFunctionProviders.byType(givenType)
                .stream()
                .map(equalityComparisonFunction -> Arguments.of(
                        equalityComparisonFunction.givenLeft(), equalityComparisonFunction.givenRight(), equalityComparisonFunction.expected()));
    }
}

