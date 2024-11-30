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

public final class StateFunctionArgumentsProvider implements ArgumentsProvider {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("IS_BLANK")
    @ParameterizedTest
    @ArgumentsSource(StateFunctionArgumentsProvider.class)
    public @interface IsBlankTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("IS_ERROR")
    @ParameterizedTest
    @ArgumentsSource(StateFunctionArgumentsProvider.class)
    public @interface IsErrorTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("IS_LOGICAL")
    @ParameterizedTest
    @ArgumentsSource(StateFunctionArgumentsProvider.class)
    public @interface IsLogicalTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("IS_NOT_AVAILABLE")
    @ParameterizedTest
    @ArgumentsSource(StateFunctionArgumentsProvider.class)
    public @interface IsNotAvailableTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("IS_NUMERIC")
    @ParameterizedTest
    @ArgumentsSource(StateFunctionArgumentsProvider.class)
    public @interface IsNumericTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("IS_TEXT")
    @ParameterizedTest
    @ArgumentsSource(StateFunctionArgumentsProvider.class)
    public @interface IsTextTest {
    }

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final StateFunctionProviders.Type givenType = context.getTags().stream()
                .filter(StateFunctionProviders.Type::matchTag)
                .map(StateFunctionProviders.Type::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Should not be here: unknown type"));
        return StateFunctionProviders.byType(givenType)
                .stream()
                .map(stateFunction -> Arguments.of(stateFunction.givenArgument(), stateFunction.expected()));
    }

}
