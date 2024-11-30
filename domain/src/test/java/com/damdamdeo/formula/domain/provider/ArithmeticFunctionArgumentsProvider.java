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

public final class ArithmeticFunctionArgumentsProvider implements ArgumentsProvider {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("ADD")
    @ParameterizedTest
    @ArgumentsSource(ArithmeticFunctionArgumentsProvider.class)
    public @interface AddTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("SUBTRACT")
    @ParameterizedTest
    @ArgumentsSource(ArithmeticFunctionArgumentsProvider.class)
    public @interface SubtractTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("MULTIPLY")
    @ParameterizedTest
    @ArgumentsSource(ArithmeticFunctionArgumentsProvider.class)
    public @interface MultiplyTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("DIVIDE")
    @ParameterizedTest
    @ArgumentsSource(ArithmeticFunctionArgumentsProvider.class)
    public @interface DivideTest {
    }

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final ArithmeticFunctionProviders.Type givenType = context.getTags().stream()
                .filter(ArithmeticFunctionProviders.Type::matchTag)
                .map(ArithmeticFunctionProviders.Type::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Should not be here: unknown type"));
        return ArithmeticFunctionProviders.byType(givenType)
                .stream()
                .map(arithmeticFunction -> Arguments.of(
                        arithmeticFunction.givenLeft(), arithmeticFunction.givenRight(), arithmeticFunction.expected()));
    }
}
