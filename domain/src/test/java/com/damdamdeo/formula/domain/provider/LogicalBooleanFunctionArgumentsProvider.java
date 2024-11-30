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

public final class LogicalBooleanFunctionArgumentsProvider implements ArgumentsProvider {
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("OR")
    @ParameterizedTest
    @ArgumentsSource(LogicalBooleanFunctionArgumentsProvider.class)
    public @interface OrTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("AND")
    @ParameterizedTest
    @ArgumentsSource(LogicalBooleanFunctionArgumentsProvider.class)
    public @interface AndTest {
    }

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final LogicalBooleanFunctionProviders.Type givenType = context.getTags().stream()
                .filter(LogicalBooleanFunctionProviders.Type::matchTag)
                .map(LogicalBooleanFunctionProviders.Type::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Should not be here: unknown type"));
        return LogicalBooleanFunctionProviders.byType(givenType)
                .stream()
                .map(logicalBooleanFunction -> Arguments.of(
                        logicalBooleanFunction.givenLeft(), logicalBooleanFunction.givenRight(), logicalBooleanFunction.expected()));
    }
}
