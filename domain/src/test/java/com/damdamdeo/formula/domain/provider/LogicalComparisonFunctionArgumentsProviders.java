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

public final class LogicalComparisonFunctionArgumentsProviders implements ArgumentsProvider {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("IF")
    @ParameterizedTest
    @ArgumentsSource(LogicalComparisonFunctionArgumentsProviders.class)
    public @interface IfTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("IF_ERROR")
    @ParameterizedTest
    @ArgumentsSource(LogicalComparisonFunctionArgumentsProviders.class)
    public @interface IfErrorTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("IF_NOT_AVAILABLE")
    @ParameterizedTest
    @ArgumentsSource(LogicalComparisonFunctionArgumentsProviders.class)
    public @interface IfNotAvailableTest {
    }

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final LogicalComparisonFunctionProviders.Type givenType = context.getTags().stream()
                .filter(LogicalComparisonFunctionProviders.Type::matchTag)
                .map(LogicalComparisonFunctionProviders.Type::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Should not be here: unknown type"));
        return LogicalComparisonFunctionProviders.byType(givenType)
                .stream()
                .map(logicalComparisonFunction -> Arguments.of(
                        logicalComparisonFunction.givenComparison(), logicalComparisonFunction.expected()));
    }

}
