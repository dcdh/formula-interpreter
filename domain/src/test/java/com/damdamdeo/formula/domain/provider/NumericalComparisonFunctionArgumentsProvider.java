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

public final class NumericalComparisonFunctionArgumentsProvider implements ArgumentsProvider {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("GREATER_THAN")
    @ParameterizedTest
    @ArgumentsSource(NumericalComparisonFunctionArgumentsProvider.class)
    public @interface GreaterThanTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("GREATER_THAN_OR_EQUAL_TO")
    @ParameterizedTest
    @ArgumentsSource(NumericalComparisonFunctionArgumentsProvider.class)
    public @interface GreaterThanOrEqualToTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("LESS_THAN")
    @ParameterizedTest
    @ArgumentsSource(NumericalComparisonFunctionArgumentsProvider.class)
    public @interface LessThanTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("LESS_THAN_OR_EQUAL_TO")
    @ParameterizedTest
    @ArgumentsSource(NumericalComparisonFunctionArgumentsProvider.class)
    public @interface LessThanOrEqualToTest {
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        final NumericalComparisonFunctionProviders.Type givenType = context.getTags().stream()
                .filter(NumericalComparisonFunctionProviders.Type::matchTag)
                .map(NumericalComparisonFunctionProviders.Type::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Should not be here: unknown type"));
        return NumericalComparisonFunctionProviders.byType(givenType)
                .stream()
                .map(numericalComparisonFunction -> Arguments.of(
                        numericalComparisonFunction.givenLeft(), numericalComparisonFunction.givenRight(), numericalComparisonFunction.expected()));
    }

}
