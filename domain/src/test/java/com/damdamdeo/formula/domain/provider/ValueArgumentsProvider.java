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

public final class ValueArgumentsProvider implements ArgumentsProvider {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("TEXT")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface TextTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("NUMERIC")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface NumericTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("BOOLEAN_TRUE")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface BooleanTrueTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("BOOLEAN_FALSE")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface BooleanFalseTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("NOT_AVAILABLE")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface NotAvailableTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("UNKNOWN_REF")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface UnknownRefTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("NOT_A_NUMERICAL")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface NotANumericalTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("DIVIDED_BY_ZERO")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface DividedByZeroTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("NOT_A_LOGICAL_VALUE")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface NotALogicalValueTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("STRUCTURED_REFERENCE_RESOLVED_NUMERIC")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface StructuredReferenceResolvedNumericTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("STRUCTURED_REFERENCE_RESOLVED_BOOLEAN")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface StructuredReferenceResolvedBooleanTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("STRUCTURED_REFERENCE_RESOLVED_TEXT")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface StructuredReferenceResolvedTextTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("STRUCTURED_REFERENCE_UNKNOWN")
    @ParameterizedTest
    @ArgumentsSource(ValueArgumentsProvider.class)
    public @interface StructuredReferenceUnknownTest {
    }

    @Override
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final ValueProviders.Type givenType = context.getTags().stream()
                .filter(ValueProviders.Type::matchTag)
                .map(ValueProviders.Type::valueOf)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Should not be here: unknown type"));
        return ValueProviders.byType(givenType)
                .stream()
                .map(GivenValue::new)
                .map(Arguments::of);
    }
}
