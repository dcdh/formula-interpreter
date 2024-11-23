package com.damdamdeo.formula.domain.usecase.resolver;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.SyntaxError;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public abstract class ValidatorParameterResolver<R extends SyntaxError> implements ParameterResolver {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("VALID")
    @Test
    public @interface ValidTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("INVALID")
    @Test
    public @interface InvalidTest {
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("INVALID")
    @Test
    public @interface ValidationExceptionTest {
    }

    public enum State {
        VALID, INVALID;

        public static boolean matchTag(final String tag) {
            for (final State state : State.values()) {
                if (state.name().equals(tag)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return List.of(GivenHappyPath.class, GivenFailing.class)
                .contains(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        final Class<?> type = parameterContext.getParameter().getType();
        if (GivenHappyPath.class.isAssignableFrom(type)) {
            final State state = extensionContext.getTags()
                    .stream()
                    .filter(State::matchTag)
                    .map(State::valueOf)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Should not be here: unknown tag"));
            return switch (state) {
                case VALID -> new GivenHappyPath<>(new Formula("true"), Optional.empty());
                case INVALID -> new GivenHappyPath<>(new Formula("IF"), Optional.of(givenInvalid()));
            };
        } else if (GivenFailing.class.isAssignableFrom(type)) {
            return givenFailings().stream()
                    .filter(failingResponse -> failingResponse.matchTag(extensionContext.getTags()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No tags match one of the givenFailingResponse"));
        } else {
            throw new ParameterResolutionException(parameterContext.getParameter().getType() + " is not supported");
        }
    }

    protected abstract R givenInvalid();

    protected abstract List<GivenFailing> givenFailings();

    public record GivenHappyPath<R>(Formula formula, Optional<R> response) {
        public GivenHappyPath {
            Objects.requireNonNull(formula);
            Objects.requireNonNull(response);
        }

        public Uni<Optional<R>> toUni() {
            return Uni.createFrom().item(response);
        }
    }

    public record GivenFailing(Formula formula, Throwable cause) {
        public GivenFailing {
            Objects.requireNonNull(formula);
            Objects.requireNonNull(cause);
        }

        public Uni<Exception> toUni() {
            return Uni.createFrom().failure(cause);
        }

        public boolean matchTag(final Set<String> tags) {
            Objects.requireNonNull(tags);
            return tags.contains(cause.getClass().getSimpleName());
        }
    }
}
